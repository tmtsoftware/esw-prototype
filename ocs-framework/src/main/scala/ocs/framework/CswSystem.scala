package ocs.framework

import akka.actor.Scheduler
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import akka.{actor, Done}
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory
import ocs.client.factory.LocationServiceWrapper
import ocs.framework.GuardianActor.GuardianMsg
import ocs.framework.dsl.epic.internal.event.MockEventService

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, ExecutionContext}

class CswSystem(name: String) {

  implicit lazy val typedSystem: ActorSystem[GuardianMsg] = ActorSystemFactory.remote[GuardianMsg](GuardianActor.behavior, name)
  implicit lazy val system: actor.ActorSystem             = typedSystem.toUntyped

  private implicit lazy val materializer: Materializer = ActorMaterializer()
  implicit lazy val executionContext: ExecutionContext = typedSystem.executionContext

  implicit lazy val scheduler: Scheduler = typedSystem.scheduler
  implicit lazy val timeout: Timeout     = Timeout(5.seconds)

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService)

//  lazy val mockEventService: EpicsEventService = new RedisEventService
  lazy val mockEventService: MockEventService = new MockEventService

  def userActorOf[T](behavior: Behavior[T], name: String, props: Props = Props.empty): ActorRef[T] = {
    Await.result(typedSystem ? GuardianActor.Spawn(behavior, name, props), 5.seconds)
  }

  def shutdownUserActors[T](): Done = {
    Await.result(typedSystem ? GuardianActor.ShutdownChildren, 10.seconds)
  }

  def createMaterializer[T](): Materializer = {
    Await.result(typedSystem ? GuardianActor.GetMaterializer, 10.seconds)
  }
}
