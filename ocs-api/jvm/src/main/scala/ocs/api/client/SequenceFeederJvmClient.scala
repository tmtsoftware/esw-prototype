package ocs.api.client

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import ocs.api.SequenceFeeder
import ocs.api.messages.SupervisorMsg
import ocs.api.models.{AggregateResponse, Sequence}
import ocs.api.messages.SequencerMsg.ProcessSequence

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong
import scala.util.Try

class SequenceFeederJvmClient(supervisor: ActorRef[SupervisorMsg])(implicit system: ActorSystem) extends SequenceFeeder {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler

  import system.dispatcher

  override def submit(commandList: Sequence): Future[AggregateResponse] = {
    val future: Future[Try[AggregateResponse]] = supervisor ? (x => ProcessSequence(commandList.commands.toList, x))
    future.map(_.get)
  }
}
