package tmt.sequencer.rpc.server

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import csw.services.event.api.scaladsl.EventService
import csw.services.event.internal.redis.RedisEventServiceFactory
import csw.services.location.commons.ActorSystemFactory
import csw.services.location.scaladsl.{LocationService, LocationServiceFactory}

import scala.concurrent.ExecutionContext

class Wiring(port: Option[Int]) {

  lazy implicit val system: ActorSystem                = ActorSystemFactory.remote()
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService               = LocationServiceFactory.makeLocalHttpClient
  lazy val locationServiceWrapper: LocationServiceGateway = new LocationServiceGateway(locationService)
  lazy val eventService: EventService                     = new RedisEventServiceFactory().make(locationService)

  lazy val configs   = new Configs(port)
  lazy val routes    = new Routes(locationServiceWrapper, eventService)
  lazy val rpcServer = new RpcServer(configs, routes)

}
