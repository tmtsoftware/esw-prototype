package tmt.sequencer.messages

import akka.Done
import akka.actor.typed.ActorRef
import tmt.sequencer.models._

import scala.util.Try

sealed trait SupervisorMsg

object SupervisorMsg {
  case class ControlCommand(name: String, replyTo: ActorRef[Try[Done]]) extends SupervisorMsg
}

sealed trait SequencerMsg

object SequencerMsg {
  case class GetNext(replyTo: ActorRef[Step])             extends SequencerMsg
  case class MaybeNext(replyTo: ActorRef[Option[Step]])   extends SequencerMsg
  case class Update(aggregateResponse: AggregateResponse) extends SequencerMsg

  sealed trait ExternalSequencerMsg extends SequencerMsg with SupervisorMsg

  case class ProcessSequence(commands: List[Command], replyTo: ActorRef[Try[AggregateResponse]]) extends ExternalSequencerMsg
  case class Add(commands: List[Command])                                                        extends ExternalSequencerMsg
  case object Pause                                                                              extends ExternalSequencerMsg
  case object Resume                                                                             extends ExternalSequencerMsg
  case object DiscardPending                                                                     extends ExternalSequencerMsg
  case class Replace(id: Id, commands: List[Command])                                            extends ExternalSequencerMsg
  case class Prepend(commands: List[Command])                                                    extends ExternalSequencerMsg
  case class Delete(ids: List[Id])                                                               extends ExternalSequencerMsg
  case class InsertAfter(id: Id, commands: List[Command])                                        extends ExternalSequencerMsg
  case class AddBreakpoints(ids: List[Id])                                                       extends ExternalSequencerMsg
  case class RemoveBreakpoints(ids: List[Id])                                                    extends ExternalSequencerMsg
  case class GetSequence(replyTo: ActorRef[Sequence])                                            extends ExternalSequencerMsg
}
