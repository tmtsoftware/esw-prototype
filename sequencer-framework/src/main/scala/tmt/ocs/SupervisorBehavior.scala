package tmt.ocs
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.Behaviors
import tmt.ocs.dsl.Script
import tmt.ocs.messages.SequencerMsg.ExternalSequencerMsg
import tmt.ocs.messages.SupervisorMsg.ControlCommand
import tmt.ocs.messages.{SequencerMsg, SupervisorMsg}

object SupervisorBehavior {
  def behavior(sequencerRef: ActorRef[SequencerMsg], script: Script): Behaviors.Receive[SupervisorMsg] =
    Behaviors.receive[SupervisorMsg] { (ctx, msg) =>
      import ctx.executionContext
      msg match {
        case ControlCommand("shutdown", replyTo) => script.shutdown().onComplete(x => replyTo ! x.map(_ => ()))
        case msg: ExternalSequencerMsg           => sequencerRef ! msg
        case _                                   =>
      }
      Behaviors.same
    }
}
