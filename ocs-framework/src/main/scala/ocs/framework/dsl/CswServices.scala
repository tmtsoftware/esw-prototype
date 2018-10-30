package ocs.framework.dsl

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import csw.command.client.CommandResponseManager
import csw.event.api.scaladsl.{EventService, EventSubscription}
import csw.params.commands.CommandResponse.SubmitResponse
import csw.params.commands.{CommandResponse, ControlCommand, SequenceCommand}
import csw.params.core.models.Id
import csw.params.events.{Event, EventKey}
import ocs.api.{SequenceEditor, SequencerCommandService}
import ocs.client.factory.{ComponentFactory, LocationServiceWrapper}
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
    val sequenceOperator: SequenceOperator, //this param is carried only to be passed to the Script
    componentFactory: ComponentFactory,
    locationService: LocationServiceWrapper,
    eventService: EventService,
    romaineFactory: RomaineFactory,
    commandResponseManager: CommandResponseManager
)(implicit system: ActorSystem) {

  private lazy val masterId: String = ConfigFactory.load().getString("csw-event.redis.masterId")

  implicit val timeout: Timeout = Timeout(10.seconds)

  private lazy val redisAsyncScalaApi: RedisAsyncApi[String, String] = {
    romaineFactory.redisAsyncApi(locationService.redisUrI(masterId))
  }

  def sequencerCommandService(subSystemSequencerId: String): Future[SequencerCommandService] =
    componentFactory.sequenceCommandService(subSystemSequencerId, observingMode)

  def sequenceEditor(subSystemSequencerId: String): Future[SequenceEditor] =
    componentFactory.sequenceEditor(subSystemSequencerId, observingMode)

  def submit(assemblyName: String, command: ControlCommand): Future[SubmitResponse] = {
    componentFactory.assemblyCommandService(assemblyName).flatMap(_.submit(command))(system.dispatcher)
  }

  def submitAndSubscribe(assemblyName: String, command: ControlCommand): Future[CommandResponse] =
    componentFactory.assemblyCommandService(assemblyName).flatMap(_.submit(command))(system.dispatcher)

  def oneway(assemblyName: String, command: ControlCommand): Future[CommandResponse] =
    componentFactory.assemblyCommandService(assemblyName).flatMap(_.oneway(command))(system.dispatcher)

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

  def addOrUpdateCommand(runId: Id, cmdStatus: SubmitResponse): Unit = {
    commandResponseManager.addOrUpdateCommand(runId, cmdStatus)
  }

  def addSubCommands(parentCommand: SequenceCommand, childCommands: Set[SequenceCommand]): Unit = {
    childCommands.foreach(
      childCommand => commandResponseManager.addSubCommand(parentCommand.runId, childCommand.runId)
    )
  }

  def updateSubCommand(subCmdId: Id, subCmdResponse: SubmitResponse): Unit = {
    commandResponseManager.updateSubCommand(subCmdId, subCmdResponse)
  }

  def queryFinalCommandStatus(runId: Id)(implicit timeout: Timeout): Future[SubmitResponse] = {
    commandResponseManager.queryFinal(runId)
  }

  def addSequenceResponse(topLevelCommandIds: Set[Id], submitResponse: SubmitResponse): Unit = {
    topLevelCommandIds.foreach(id => commandResponseManager.addOrUpdateCommand(id, CommandResponse.withRunId(id, submitResponse)))
  }
}
