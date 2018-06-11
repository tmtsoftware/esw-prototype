package tmt.sequencer.rpc.server

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import tmt.sequencer.api.SequenceEditor
import tmt.sequencer.dsl.Script
import tmt.sequencer.messages.SequencerMsg
import tmt.sequencer.messages.SequencerMsg._
import tmt.sequencer.models._

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong

class SequenceEditorImpl(sequencer: ActorRef[SequencerMsg], script: Script)(implicit system: ActorSystem) extends SequenceEditor {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler
  import system.dispatcher

  override def sequence: Future[Sequence] = sequencer ? GetSequence

  override def addAll(commands: List[Command]): Future[Unit]              = Future(sequencer ! Add(commands))
  override def delete(ids: List[Id]): Future[Unit]                        = Future(sequencer ! Delete(ids))
  override def insertAfter(id: Id, commands: List[Command]): Future[Unit] = Future(sequencer ! InsertAfter(id, commands))
  override def prepend(commands: List[Command]): Future[Unit]             = Future(sequencer ! Prepend(commands))
  override def replace(id: Id, commands: List[Command]): Future[Unit]     = Future(sequencer ! Replace(id, commands))
  override def reset(): Future[Unit]                                      = Future(sequencer ! DiscardPending)

  override def pause(): Future[Unit]                          = Future(sequencer ! Pause)
  override def resume(): Future[Unit]                         = Future(sequencer ! Resume)
  override def addBreakpoints(ids: List[Id]): Future[Unit]    = Future(sequencer ! AddBreakpoints(ids))
  override def removeBreakpoints(ids: List[Id]): Future[Unit] = Future(sequencer ! RemoveBreakpoints(ids))

  override def shutdown(): Future[Unit] = script.shutdown().map(_ => ())
}
