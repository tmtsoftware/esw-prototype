package ocs.api.client

import akka.actor.typed.{ActorRef, ActorSystem, SpawnProtocol}
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.Scheduler
import akka.util.Timeout
import csw.params.commands.SequenceCommand
import csw.params.core.models.Id
import ocs.api.SequenceEditor
import ocs.api.messages.SupervisorMsg
import ocs.api.models.StepList
import ocs.api.messages.SequencerMsg._
import ocs.api.messages.SupervisorMsg.{Abort, Shutdown}

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong
import scala.util.Try

class SequenceEditorJvmClient(supervisor: ActorRef[SupervisorMsg])(implicit system: ActorSystem[_]) extends SequenceEditor {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler
  import system.executionContext

  def responseHelper[T](future: Future[Try[T]]): Future[T] = future.map(_.get)

  def sequenceCommandsFrom(commands: List[SequenceCommand]): List[SequenceCommand] = commands.map(cmd => cmd)

  override def status: Future[StepList] = {
    val future: Future[Try[StepList]] = supervisor ? (x => GetSequence(x))
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

  override def shutdown(): Future[Unit] = responseHelper(supervisor ? Shutdown)

  override def abort(): Future[Unit] = responseHelper(supervisor ? Abort)

  override def isAvailable: Future[Boolean] = status.map(seq => seq.isFinished)
}
