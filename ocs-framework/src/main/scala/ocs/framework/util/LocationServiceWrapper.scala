package ocs.framework.util

import akka.actor.typed.ActorRef
import akka.actor.{ActorSystem, CoordinatedShutdown}
import csw.location.api.models.Connection.{AkkaConnection, TcpConnection}
import csw.location.api.models.{AkkaLocation, AkkaRegistration, ComponentId, ComponentType}
import csw.location.api.scaladsl.LocationService
import csw.params.core.models.Prefix
import io.lettuce.core.RedisURI
import ocs.api.messages.SupervisorMsg

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContext, Future}

class LocationServiceWrapper(locationService: LocationService, system: ActorSystem)(implicit ec: ExecutionContext) {

  def register(componentName: String, componentType: ComponentType, supervisorRef: ActorRef[SupervisorMsg]): Unit = {
    val registration =
      AkkaRegistration(
        AkkaConnection(ComponentId(componentName, componentType)),
        Prefix("sequencer"),
        supervisorRef,
      )

    println(s"Registering [${registration.actorRef.path}]")
    locationService.register(registration).foreach { registrationResult =>
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
