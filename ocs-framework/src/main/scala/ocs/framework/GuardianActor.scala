package ocs.framework

import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, Props}
import akka.stream.Materializer
import akka.stream.typed.scaladsl.ActorMaterializer

object GuardianActor {
  sealed trait GuardianMsg
  case class Spawn[T](behavior: Behavior[T], name: String, props: Props)(val replyTo: ActorRef[ActorRef[T]]) extends GuardianMsg
  case class ShutdownChildren(replyTo: ActorRef[Done])                                                       extends GuardianMsg
  case class GetMaterializer(replyTo: ActorRef[Materializer])                                                extends GuardianMsg
  private case class ShutdownReply(replyTo: ActorRef[Done])                                                  extends GuardianMsg

  val behavior: Behavior[GuardianMsg] = Behaviors.setup[GuardianMsg] { ctx =>
    Behaviors.receiveMessage[GuardianMsg] {
      case msg @ Spawn(beh, name, props) =>
        val actorRef = ctx.spawn(beh, name, props)
        msg.replyTo ! actorRef
        Behaviors.same

      // todo: do we want to shutdown all the children?
      case msg @ ShutdownChildren(replyTo) =>
        if (ctx.children.isEmpty) {
          msg.replyTo ! Done
        } else {
          ctx.children.foreach { child =>
            ctx.stop(child)
            // todo: watch first and then stop?
            ctx.watchWith(child, ShutdownReply(replyTo))
          }
        }
        Behaviors.same
      case ShutdownReply(replyTo) =>
        if (ctx.children.isEmpty) {
          replyTo ! Done
        }
        Behaviors.same
      case GetMaterializer(replyTo) =>
        // todo: should we cache materializer?
        //  Every time it creates new stream supervisor actor
        replyTo ! ActorMaterializer.boundToActor(ctx)
        Behaviors.same
    }
  }
}
