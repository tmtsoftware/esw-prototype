package ocs.client

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
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

  lazy implicit val system: ActorSystem                = ActorSystemFactory.remote()
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val eventService: EventService                     = new EventServiceFactory().make(locationService)
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService, system)

  lazy val componentFactory = new ComponentFactory(locationServiceWrapper)
}
