package ocs.api.client

import akka.Done
import akka.actor.typed.{ActorRef, ActorSystem, SpawnProtocol}
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.Scheduler
import akka.util.Timeout
import csw.location.api.models.AkkaLocation
import ocs.api.SequenceComponentApi
import ocs.api.messages.SequenceComponentMsg
import ocs.api.messages.SequenceComponentMsg.{GetStatus, LoadScript, StopScript}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class SequenceComponentJvmClient(sequenceComponentRef: ActorRef[SequenceComponentMsg], system: ActorSystem[_])
    extends SequenceComponentApi {

  private implicit val timeout: Timeout     = Timeout(10.seconds)
  private implicit val scheduler: Scheduler = system.scheduler

  override def loadScript(sequencerId: String, observingMode: String): Future[Either[AkkaLocation, AkkaLocation]] = {
    sequenceComponentRef ? (x => LoadScript(sequencerId, observingMode, x))
  }

  override def stopScript: Future[Done]                = sequenceComponentRef ? StopScript
  override def getStatus: Future[Option[AkkaLocation]] = sequenceComponentRef ? GetStatus
}
