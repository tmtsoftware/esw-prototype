package ocs.framework

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory
import ocs.framework.wrapper.{ComponentFactory, LocationServiceWrapper}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class TestAmmoniteWiring() {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy implicit val system: ActorSystem                = ActorSystemFactory.remote()
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService, system)

  lazy val componentFactory = new ComponentFactory(locationServiceWrapper)
}
