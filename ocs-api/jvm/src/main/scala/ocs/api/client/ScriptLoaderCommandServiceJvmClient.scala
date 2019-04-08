package ocs.api.client
import akka.Done
import akka.actor.{ActorSystem, Scheduler}
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.ActorRef
import akka.util.Timeout
import csw.location.api.models.ComponentId
import ocs.api.ScriptLoaderCommandService
import ocs.api.messages.ScriptLoaderMsg
import ocs.api.messages.ScriptLoaderMsg.{GetStatus, LoadScript, StopScript}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class ScriptLoaderCommandServiceJvmClient(scriptLoaderRef: ActorRef[ScriptLoaderMsg], system: ActorSystem)
    extends ScriptLoaderCommandService {

  private implicit val timeout: Timeout     = Timeout(10.seconds)
  private implicit val scheduler: Scheduler = system.scheduler

  override def loadScript(sequencerId: String, observingMode: String): Future[Done] =
    scriptLoaderRef ? (x => LoadScript(sequencerId, observingMode, x))
  override def stopScript: Future[Done]       = scriptLoaderRef ? StopScript
  override def getStatus: Future[ComponentId] = scriptLoaderRef ? GetStatus
}
