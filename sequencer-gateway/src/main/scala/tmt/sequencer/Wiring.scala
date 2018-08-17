package tmt.sequencer

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import csw.services.event.EventServiceFactory
import csw.services.event.api.scaladsl.EventService
import csw.services.location.commons.ActorSystemFactory
import csw.services.location.scaladsl.{LocationService, LocationServiceFactory}
import io.lettuce.core.RedisClient
import romaine.RomaineFactory
import tmt.sequencer.server.{Routes, Server}

import scala.concurrent.ExecutionContext

class Wiring(port: Option[Int]) {

  lazy implicit val system: ActorSystem                = ActorSystemFactory.remote()
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService               = LocationServiceFactory.makeLocalHttpClient
  lazy val locationServiceWrapper: LocationServiceGateway = new LocationServiceGateway(locationService)
  lazy val eventService: EventService                     = new EventServiceFactory().make(locationService)

  lazy val configs = new Configs(port)

  lazy val redisClient: RedisClient       = RedisClient.create()
  lazy val romaineFactory: RomaineFactory = new RomaineFactory(redisClient)

  lazy val sequencerMonitor = new SequencerMonitor(locationServiceWrapper, romaineFactory)

  lazy val routes = new Routes(locationServiceWrapper, sequencerMonitor)
  lazy val server = new Server(configs, routes)
}
