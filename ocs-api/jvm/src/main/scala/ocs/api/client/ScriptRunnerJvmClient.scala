package ocs.api.client

import akka.Done
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import csw.location.api.models.AkkaLocation
import ocs.api.ScriptRunnerApi
import ocs.api.messages.ScriptCommand
import ocs.api.messages.ScriptCommand.{GetStatus, LoadScript, StopScript}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class ScriptRunnerJvmClient(scriptRunnerRef: ActorRef[ScriptCommand], system: ActorSystem) extends ScriptRunnerApi {

  private implicit val timeout: Timeout     = Timeout(10.seconds)
  private implicit val scheduler: Scheduler = system.scheduler

  override def loadScript(sequencerId: String, observingMode: String): Future[Either[AkkaLocation, AkkaLocation]] = {
    scriptRunnerRef ? (x => LoadScript(sequencerId, observingMode, x))
  }

  override def stopScript: Future[Done]                = scriptRunnerRef ? StopScript
  override def getStatus: Future[Option[AkkaLocation]] = scriptRunnerRef ? GetStatus
}
