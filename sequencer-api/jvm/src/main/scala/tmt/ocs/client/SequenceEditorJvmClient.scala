package tmt.ocs.client

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id
import tmt.ocs.api.SequenceEditor
import tmt.ocs.models.Sequence
import tmt.ocs.messages.SequencerMsg._
import tmt.ocs.messages.SupervisorMsg
import tmt.ocs.messages.SupervisorMsg.ControlCommand

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong
import scala.util.Try

class SequenceEditorJvmClient(supervisor: ActorRef[SupervisorMsg])(implicit system: ActorSystem) extends SequenceEditor {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler
  import system.dispatcher

  def responseHelper[T](future: Future[Try[T]]): Future[T] = future.map(_.get)

  def sequenceCommandsFrom(commands: List[SequenceCommand]): List[SequenceCommand] = commands.map(cmd => cmd)

  override def sequence: Future[Sequence] = {
    val future: Future[Try[Sequence]] = supervisor ? (x => GetSequence(x))
    future.map(_.get)
  }

  override def addAll(commands: List[SequenceCommand]): Future[Unit] =
    responseHelper(supervisor ? (x => Add(sequenceCommandsFrom(commands), x)))

  override def delete(ids: List[Id]): Future[Unit] = responseHelper(supervisor ? (x => Delete(ids, x)))

  override def insertAfter(id: Id, commands: List[SequenceCommand]): Future[Unit] =
    responseHelper(supervisor ? (x => InsertAfter(id, sequenceCommandsFrom(commands), x)))

  override def prepend(commands: List[SequenceCommand]): Future[Unit] =
    responseHelper(supervisor ? (x => Prepend(sequenceCommandsFrom(commands), x)))

  override def replace(id: Id, commands: List[SequenceCommand]): Future[Unit] =
    responseHelper(supervisor ? (x => Replace(id, sequenceCommandsFrom(commands), x)))

  override def reset(): Future[Unit] = responseHelper(supervisor ? (x => DiscardPending(x)))

  override def pause(): Future[Unit] = responseHelper(supervisor ? (x => Pause(x)))

  override def resume(): Future[Unit] = responseHelper(supervisor ? (x => Resume(x)))

  override def addBreakpoints(ids: List[Id]): Future[Unit] = responseHelper(supervisor ? (x => AddBreakpoints(ids, x)))

  override def removeBreakpoints(ids: List[Id]): Future[Unit] =
    responseHelper(supervisor ? (x => RemoveBreakpoints(ids, x)))

  override def shutdown(): Future[Unit] = responseHelper(supervisor ? (x => ControlCommand("shutdown", x)))

  override def isAvailable: Future[Boolean] = sequence.map(seq => seq.isFinished)
}
