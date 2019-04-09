package ocs.client.factory

import akka.actor.ActorSystem
import akka.actor.typed.ActorRef
import csw.location.api.models.Connection.{AkkaConnection, TcpConnection}
import csw.location.api.models._
import csw.location.api.scaladsl.LocationService
import csw.params.core.models.Prefix
import io.lettuce.core.RedisURI

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContext, Future}

class LocationServiceWrapper(locationService: LocationService, system: ActorSystem)(implicit ec: ExecutionContext) {

  def register[T](prefix: Prefix, componentId: ComponentId, actorRef: ActorRef[T]): Future[RegistrationResult] = {
    val registration = AkkaRegistration(AkkaConnection(componentId), prefix, actorRef)
    println(s"Registering [${registration.actorRef.path}]")
    val eventualResult = locationService.register(registration)
    eventualResult.foreach { registrationResult =>
      println(s"Successfully registered ${componentId.name} - $registrationResult")
    }
    eventualResult
  }

  def resolve[T](componentName: String, componentType: ComponentType)(f: AkkaLocation => T): Future[T] =
    locationService
      .resolve(AkkaConnection(ComponentId(componentName, componentType)), 5.seconds)
      .map {
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
        case None =>
          throw new IllegalArgumentException(s"Could not find component - Event server")
      }
  }

  def listSequencers(): Future[List[Location]] = locationService.list(ComponentType.Sequencer)
  def listAssemblies(): Future[List[Location]] = locationService.list(ComponentType.Assembly)
}
