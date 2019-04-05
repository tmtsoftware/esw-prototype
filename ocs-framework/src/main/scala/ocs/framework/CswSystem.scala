package ocs.framework

import akka.Done
import akka.actor.Scheduler
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Props}
import ocs.framework.GuardianActor.GuardianMsg
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble

class CswSystem(name: String) {
  lazy val system: ActorSystem[GuardianMsg] = ActorSystem(GuardianActor.behavior, name)

  implicit lazy val scheduler: Scheduler = system.scheduler
  implicit lazy val timeout: Timeout     = Timeout(5.seconds)

  def spawn[T](behavior: Behavior[T], name: String, props: Props = Props.empty): ActorRef[T] = {
    Await.result(system ? GuardianActor.Spawn(behavior, name, props), 5.seconds)
  }

  def shutdownChildren[T](): Done = {
    Await.result(system ? GuardianActor.ShutdownChildren, 10.seconds)
  }
}
