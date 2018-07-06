package tmt.sequencer.rpc.server

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id
import tmt.sequencer.api.SequenceEditor
import tmt.sequencer.dsl.Script
import tmt.sequencer.messages.SequencerMsg._
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.models._

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong

class SequenceEditorImpl(supervisor: ActorRef[SupervisorMsg], script: Script)(implicit system: ActorSystem)
    extends SequenceEditor {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler
  import system.dispatcher

  def sequenceCommandsFrom(commands: List[SequenceCommandWeb]): List[SequenceCommand] =
    commands.map(cmd => SequenceCommandConversions.asSequenceCommand(cmd))

  override def sequence: Future[Sequence] = supervisor ? GetSequence

  override def addAll(commands: List[SequenceCommandWeb]): Future[Unit] = Future(supervisor ! Add(sequenceCommandsFrom(commands)))
  override def delete(ids: List[Id]): Future[Unit]                      = Future(supervisor ! Delete(ids))

  override def insertAfter(id: Id, commands: List[SequenceCommandWeb]): Future[Unit] =
    Future(supervisor ! InsertAfter(id, sequenceCommandsFrom(commands)))

  override def prepend(commands: List[SequenceCommandWeb]): Future[Unit] =
    Future(supervisor ! Prepend(sequenceCommandsFrom(commands)))

  override def replace(id: Id, commands: List[SequenceCommandWeb]): Future[Unit] =
    Future(supervisor ! Replace(id, sequenceCommandsFrom(commands)))

  override def reset(): Future[Unit] = Future(supervisor ! DiscardPending)

  override def pause(): Future[Unit]                          = Future(supervisor ! Pause)
  override def resume(): Future[Unit]                         = Future(supervisor ! Resume)
  override def addBreakpoints(ids: List[Id]): Future[Unit]    = Future(supervisor ! AddBreakpoints(ids))
  override def removeBreakpoints(ids: List[Id]): Future[Unit] = Future(supervisor ! RemoveBreakpoints(ids))

  override def shutdown(): Future[Unit] = script.shutdown().map(_ => ())
}
