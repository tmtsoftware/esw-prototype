package ocs.api.messages

import akka.Done
import akka.actor.typed.ActorRef
import csw.location.api.models.ComponentId
import csw.params.commands.CommandResponse.SubmitResponse
import csw.params.commands.SequenceCommand
import csw.params.core.models.Id
import csw.serializable.TMTSerializable
import ocs.api.models._

import scala.util.Try

sealed trait SupervisorMsg extends TMTSerializable

object SupervisorMsg {
  case class Shutdown(replyTo: ActorRef[Try[Unit]]) extends SupervisorMsg
  case class Abort(replyTo: ActorRef[Try[Unit]])    extends SupervisorMsg
}

sealed trait SequencerMsg extends TMTSerializable

object SequencerMsg {
  sealed trait InternalSequencerMsg extends SequencerMsg

  case class GetNext(replyTo: ActorRef[Step])            extends InternalSequencerMsg
  case class MaybeNext(replyTo: ActorRef[Option[Step]])  extends InternalSequencerMsg
  case class Update(submitResponse: SubmitResponse)      extends InternalSequencerMsg
  case class ReadyToExecuteNext(replyTo: ActorRef[Done]) extends InternalSequencerMsg

  sealed trait ExternalSequencerMsg extends SequencerMsg with SupervisorMsg {
    def replyTo: ActorRef[Try[Nothing]]
  }

  case class ProcessSequence(sequence: Sequence, replyTo: ActorRef[Try[SubmitResponse]])        extends ExternalSequencerMsg
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
  case class GetSequence(replyTo: ActorRef[Try[StepList]])                                      extends ExternalSequencerMsg
}

sealed trait ScriptLoaderMsg extends TMTSerializable

object ScriptLoaderMsg {
  case class LoadScript(sequencerId: String, observingMode: String, replyTo: ActorRef[Done]) extends ScriptLoaderMsg
  case class StopScript(replyTo: ActorRef[Done])                                             extends ScriptLoaderMsg
  case class GetStatus(replyTo: ActorRef[ComponentId])                                       extends ScriptLoaderMsg
}
