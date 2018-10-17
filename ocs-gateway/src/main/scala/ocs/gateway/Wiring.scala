package ocs.gateway

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory
import io.lettuce.core.RedisClient
import ocs.client.factory.{ComponentFactory, LocationServiceWrapper}
import ocs.gateway.assembly.{AssemblyService, PositionTracker}
import ocs.gateway.server.{Routes, Server}
import romaine.RomaineFactory

import scala.concurrent.ExecutionContext

class Wiring(port: Option[Int]) {

  lazy implicit val system: ActorSystem                = ActorSystemFactory.remote()
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService, system)
  lazy val eventService: EventService                     = new EventServiceFactory().make(locationService)
  lazy val componentFactory                               = new ComponentFactory(locationServiceWrapper)

  lazy val configs = new Configs(port)

  lazy val redisClient: RedisClient       = RedisClient.create()
  lazy val romaineFactory: RomaineFactory = new RomaineFactory(redisClient)

  lazy val sequencerMonitor = new SequencerMonitor(locationServiceWrapper, romaineFactory)
  lazy val eventMonitor     = new EventMonitor(eventService)
  lazy val assemblyService  = new AssemblyService(locationServiceWrapper, componentFactory)
  lazy val positionTracker  = new PositionTracker(assemblyService)

  lazy val routes =
    new Routes(locationServiceWrapper, componentFactory, sequencerMonitor, positionTracker, assemblyService, eventMonitor)
  lazy val server = new Server(configs, routes)
}
