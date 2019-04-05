package ocs.framework.messages
import akka.Done
import akka.actor.typed.ActorRef
import csw.location.api.models.ComponentId

sealed trait ScriptLoaderMsg

object ScriptLoaderMsg {
  case class LoadScript(sequencerId: String, observingMode: String, replyTo: ActorRef[Done]) extends ScriptLoaderMsg
  case class StopScript(replyTo: ActorRef[Done])                                             extends ScriptLoaderMsg
  case class GetStatus(replyTo: ActorRef[ComponentId])                                       extends ScriptLoaderMsg
}
