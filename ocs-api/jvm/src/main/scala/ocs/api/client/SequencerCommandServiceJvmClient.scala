package ocs.api.client

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import csw.params.commands.CommandResponse.SubmitResponse
import ocs.api.SequencerCommandService
import ocs.api.messages.SequencerMsg.ProcessSequence
import ocs.api.messages.SupervisorMsg
import ocs.api.models.Sequence

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong
import scala.util.Try

class SequencerCommandServiceJvmClient(supervisor: ActorRef[SupervisorMsg])(implicit system: ActorSystem)
    extends SequencerCommandService {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler

  import system.dispatcher

  override def submit(sequence: Sequence): Future[SubmitResponse] = {
    val future: Future[Try[SubmitResponse]] = supervisor ? (x => ProcessSequence(sequence, x))
    future.map(_.get)
  }
}
