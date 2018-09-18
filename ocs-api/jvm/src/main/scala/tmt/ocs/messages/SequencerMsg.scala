package tmt.ocs.messages

import akka.actor.typed.ActorRef
import csw.params.commands.SequenceCommand
import csw.params.core.models.Id
import csw.serializable.TMTSerializable
import tmt.ocs.models._

import scala.util.Try

sealed trait SupervisorMsg extends TMTSerializable

object SupervisorMsg {
  case class ControlCommand(name: String, replyTo: ActorRef[Try[Unit]]) extends SupervisorMsg
}

sealed trait SequencerMsg extends TMTSerializable

object SequencerMsg {
  case class GetNext(replyTo: ActorRef[Step])             extends SequencerMsg
  case class MaybeNext(replyTo: ActorRef[Option[Step]])   extends SequencerMsg
  case class Update(aggregateResponse: AggregateResponse) extends SequencerMsg

  sealed trait ExternalSequencerMsg extends SequencerMsg with SupervisorMsg {
    def replyTo: ActorRef[Try[Nothing]]
  }

  case class ProcessSequence(commands: List[SequenceCommand], replyTo: ActorRef[Try[AggregateResponse]])
      extends ExternalSequencerMsg
  case class Add(commands: List[SequenceCommand], replyTo: ActorRef[Try[Unit]])                 extends ExternalSequencerMsg
  case class Pause(replyTo: ActorRef[Try[Unit]])                                                extends ExternalSequencerMsg
  case class Resume(replyTo: ActorRef[Try[Unit]])                                               extends ExternalSequencerMsg
  case class DiscardPending(replyTo: ActorRef[Try[Unit]])                                       extends ExternalSequencerMsg
  case class Replace(id: Id, commands: List[SequenceCommand], replyTo: ActorRef[Try[Unit]])     extends ExternalSequencerMsg
  case class Prepend(commands: List[SequenceCommand], replyTo: ActorRef[Try[Unit]])             extends ExternalSequencerMsg
  case class Delete(ids: List[Id], replyTo: ActorRef[Try[Unit]])                                extends ExternalSequencerMsg
  case class InsertAfter(id: Id, commands: List[SequenceCommand], replyTo: ActorRef[Try[Unit]]) extends ExternalSequencerMsg
  case class AddBreakpoints(ids: List[Id], replyTo: ActorRef[Try[Unit]])                        extends ExternalSequencerMsg
  case class RemoveBreakpoints(ids: List[Id], replyTo: ActorRef[Try[Unit]])                     extends ExternalSequencerMsg
  case class GetSequence(replyTo: ActorRef[Try[Sequence]])                                      extends ExternalSequencerMsg
}
