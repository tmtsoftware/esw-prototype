package ocs.client

import akka.actor.typed.{ActorSystem, SpawnProtocol}
import akka.stream.Materializer
import akka.stream.typed.scaladsl.ActorMaterializer
import akka.util.Timeout
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory
import ocs.client.factory.{ComponentFactory, LocationServiceWrapper}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Wiring() {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy implicit val typedSystem: ActorSystem[SpawnProtocol] = ActorSystemFactory.remote(SpawnProtocol.behavior, "ocs-client")
  lazy implicit val materializer: Materializer              = ActorMaterializer()(typedSystem)
  lazy implicit val executionContext: ExecutionContext      = typedSystem.executionContext

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val eventService: EventService                     = new EventServiceFactory().make(locationService)
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService)

  lazy val componentFactory = new ComponentFactory(locationServiceWrapper)
}
