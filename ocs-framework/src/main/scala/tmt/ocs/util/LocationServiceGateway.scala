package tmt.ocs.util

import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.{typed, ActorSystem, CoordinatedShutdown}
import csw.location.api.models.Connection.{AkkaConnection, TcpConnection}
import csw.location.api.models.{AkkaLocation, AkkaRegistration, ComponentId, ComponentType}
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.logging.messages.LogControlMessages
import csw.params.core.models.Prefix
import io.lettuce.core.RedisURI
import tmt.ocs.messages.SupervisorMsg

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContext, Future}

class LocationServiceGateway(locationService: LocationService, system: ActorSystem)(implicit ec: ExecutionContext) {

  def register(componentName: String, componentType: ComponentType, supervisorRef: ActorRef[SupervisorMsg]): Future[Unit] = {
    val dummyLogAdminActorRef: typed.ActorRef[LogControlMessages] =
      ActorSystemFactory.remote().spawn(Behavior.empty, "dummy-log-admin-actor-ref")

    val registration =
      AkkaRegistration(AkkaConnection(ComponentId(componentName, componentType)),
                       Prefix("sequencer"),
                       supervisorRef,
                       dummyLogAdminActorRef)

    println(s"Registering [${registration.logAdminActorRef.path}]")
    locationService.register(registration).map { registrationResult =>
      println(s"Successfully registered $componentName - $registrationResult")

      CoordinatedShutdown(system).addTask(
        CoordinatedShutdown.PhaseBeforeServiceUnbind,
        s"unregistering-${registrationResult.location}"
      ) { () =>
        println(s"Shutting down actor system, unregistering-${registrationResult.location}")
        registrationResult.unregister()
      }
    }

  }

  def resolve[T](componentName: String, componentType: ComponentType)(f: AkkaLocation => Future[T]): Future[T] =
    locationService
      .resolve(AkkaConnection(ComponentId(componentName, componentType)), 5.seconds)
      .flatMap {
        case Some(akkaLocation) =>
          f(akkaLocation)
        case None =>
          throw new IllegalArgumentException(s"Could not find component - $componentName of type - $componentType")
      }

  def redisUrI(masterId: String): Future[RedisURI] = {
    locationService
      .resolve(TcpConnection(ComponentId("EventServer", ComponentType.Service)), 5.seconds)
      .flatMap {
        case Some(tcpLocation) =>
          Future { RedisURI.Builder.sentinel(tcpLocation.uri.getHost, tcpLocation.uri.getPort, masterId).build() }
        case None => throw new IllegalArgumentException(s"Could not find component - Event server")
      }
  }
}
