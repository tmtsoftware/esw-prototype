package ocs.framework.dsl

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import csw.event.api.scaladsl.{EventService, EventSubscription}
import csw.params.commands.{CommandResponse, ControlCommand}
import csw.params.events.{Event, EventKey}
import ocs.api.{SequenceEditor, SequenceFeeder}
import ocs.factory.{ComponentFactory, LocationServiceWrapper}
import ocs.framework.ScriptImports.toDuration
import ocs.framework.core.SequenceOperator
import romaine.RomaineFactory
import romaine.async.RedisAsyncApi
import sequencer.macros.StrandEc

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class CswServices(
    val sequencerId: String,
    val observingMode: String,
    val sequencer: SequenceOperator, //this param is carried only to be passed to the Script
    componentFactory: ComponentFactory,
    locationService: LocationServiceWrapper,
    eventService: EventService,
    romaineFactory: RomaineFactory
)(implicit system: ActorSystem) {

  private lazy val masterId: String = ConfigFactory.load().getString("csw-event.redis.masterId")

  implicit val timeout: Timeout = Timeout(10.seconds)

  private lazy val redisAsyncScalaApi: RedisAsyncApi[String, String] = {
    romaineFactory.redisAsyncApi(locationService.redisUrI(masterId))
  }

  def sequenceFeeder(subSystemSequencerId: String): Future[SequenceFeeder] =
    componentFactory.sequenceFeeder(subSystemSequencerId, observingMode)

  def sequenceEditor(subSystemSequencerId: String): Future[SequenceEditor] =
    componentFactory.sequenceEditor(subSystemSequencerId, observingMode)

  def submit(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    componentFactory.assembly(assemblyName).flatMap(_.submit(command))(system.dispatcher)
  }

  def submitAndSubscribe(assemblyName: String, command: ControlCommand): Future[CommandResponse] =
    componentFactory.assembly(assemblyName).flatMap(_.complete(command))(system.dispatcher)

  def oneway(assemblyName: String, command: ControlCommand): Future[CommandResponse] =
    componentFactory.assembly(assemblyName).flatMap(_.oneway(command))(system.dispatcher)

  def subscribe(eventKeys: Set[EventKey])(callback: Event => Done)(implicit strandEc: StrandEc): EventSubscription = {
    println(s"==========================> Subscribing event $eventKeys")
    eventService.defaultSubscriber.subscribeAsync(eventKeys, e => Future(callback(e))(strandEc.ec))
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => Event)(implicit strandEc: StrandEc): Cancellable = {
    println(s"=========================> Publishing event $eventGeneratorBlock every $every")
    eventService.defaultPublisher.publishAsync(Future(eventGeneratorBlock)(strandEc.ec), every)
  }

  def publish(event: Event): Future[Done] = {
    println(s"=========================> Publishing event $event")
    eventService.defaultPublisher.publish(event)
  }

  def sendResult(msg: String): Future[Done] = {
    redisAsyncScalaApi.publish(s"$sequencerId-$observingMode", msg).map(_ => Done)(system.dispatcher)
  }
}
