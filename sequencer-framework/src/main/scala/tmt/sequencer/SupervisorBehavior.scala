package tmt.sequencer

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.Behaviors
import tmt.sequencer.ScriptImports.Script
import tmt.sequencer.messages.SequencerMsg.ExternalSequencerMsg
import tmt.sequencer.messages.SupervisorMsg.ControlCommand
import tmt.sequencer.messages.{SequencerMsg, SupervisorMsg}

object SupervisorBehavior {
  def behavior(sequencerRef: ActorRef[SequencerMsg], script: Script): Behaviors.Receive[SupervisorMsg] =
    Behaviors.receive[SupervisorMsg] { (ctx, msg) =>
      import ctx.executionContext
      msg match {
        case ControlCommand("shutdown", replyTo) => script.shutdown().onComplete(x => replyTo ! x)
        case msg: ExternalSequencerMsg           => sequencerRef ! msg
        case _                                   =>
      }
      Behaviors.same
    }
}
