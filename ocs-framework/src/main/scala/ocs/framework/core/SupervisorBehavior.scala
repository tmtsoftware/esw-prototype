package ocs.framework.core
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.Behaviors
import ocs.api.messages.SequencerMsg.ExternalSequencerMsg
import ocs.api.messages.SupervisorMsg.{Shutdown, Start, Stop}
import ocs.api.messages.{SequencerMsg, SupervisorMsg}
import ocs.framework.dsl.Script

object SupervisorBehavior {
  def behavior(sequencerRef: ActorRef[SequencerMsg], script: Script): Behaviors.Receive[SupervisorMsg] =
    Behaviors.receive[SupervisorMsg] { (ctx, msg) =>
      import ctx.executionContext
      msg match {
        case Shutdown(replyTo)         => script.shutdown().onComplete(x => replyTo ! x.map(_ => ()))
        case Start(replyTo)            => script.onStart().onComplete(x => replyTo ! x.map(_ => ()))
        case Stop(replyTo)             => script.onStop().onComplete(x => replyTo ! x.map(_ => ()))
        case msg: ExternalSequencerMsg => sequencerRef ! msg
        case _                         =>
      }
      Behaviors.same
    }
}
