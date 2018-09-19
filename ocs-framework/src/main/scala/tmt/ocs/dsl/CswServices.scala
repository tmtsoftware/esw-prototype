package tmt.ocs.dsl

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{typed, ActorSystem, Cancellable}
import akka.util.Timeout
import akka.{util, Done}
import com.typesafe.config.ConfigFactory
import csw.command.scaladsl.CommandService
import csw.event.api.scaladsl.{EventService, EventSubscription}
import csw.location.api.models.ComponentType
import csw.params.commands.{CommandResponse, ControlCommand, SequenceCommand, Setup}
import csw.params.events.{Event, EventKey}
import romaine.RomaineFactory
import romaine.async.RedisAsyncApi
import tmt.ocs.api.SequenceFeeder
import tmt.ocs.client.SequenceFeederJvmClient
import tmt.ocs.messages.SupervisorMsg
import tmt.ocs.util._
import tmt.ocs.{Engine, Sequencer, SequencerUtil}

import scala.async.Async._
import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{Await, Future}

class CswServices(
    sequencer: Sequencer,
    engine: Engine,
    locationService: LocationServiceGateway,
    eventService: EventService,
    romaineFactory: RomaineFactory,
    val sequencerId: String,
    val observingMode: String
)(implicit system: ActorSystem)
    extends ScriptDsl {

  implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  private lazy val masterId: String = ConfigFactory.load().getString("csw-event.redis.masterId")

  private lazy val redisAsyncScalaApi: RedisAsyncApi[String, String] = {
    romaineFactory.redisAsyncApi(locationService.redisUrI(masterId))
  }

  def sequenceFeeder(subSystemSequencerId: String): SequenceFeeder = {
    val componentName = SequencerUtil.getComponentName(subSystemSequencerId, observingMode)
    val eventualFeederImpl = locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederJvmClient(supervisorRef)
      }(system.dispatcher)
    }
    Await.result(eventualFeederImpl, 5.seconds)
  }

  def setup(assemblyName: String, command: SequenceCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        val setupCommand: Setup       = CswCommandAdapter.setupCommandFrom(command)
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).submit(setupCommand))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

  def submit(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).submit(command))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

  def submitAndSubscribe(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).submitAndSubscribe(command))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

  def oneway(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).oneway(command))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

  def subscribe(eventKeys: Set[EventKey])(callback: Event => Done): EventSubscription = {
    println(s"==========================> Subscribing event $eventKeys")
    eventService.defaultSubscriber.subscribeAsync(eventKeys, e => spawn(callback(e)))
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => Event): Cancellable = {
    println(s"=========================> Publishing event $eventGeneratorBlock every $every")
    eventService.defaultPublisher.publish(eventGeneratorBlock, every)
  }

  def publish(event: Event): Future[Done] = {
    println(s"=========================> Publishing event $event")
    eventService.defaultPublisher.publish(event)
  }

  def sendResult(msg: String): Future[Done] = {
    redisAsyncScalaApi.publish(s"$sequencerId-$observingMode", msg).map(_ => Done)(system.dispatcher)
  }
}
