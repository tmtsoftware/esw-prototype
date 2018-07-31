package tmt.sequencer.rpc.server

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import csw.services.event.api.scaladsl.EventService
import csw.services.event.internal.redis.RedisEventServiceFactory
import csw.services.location.commons.ClusterSettings
import csw.services.location.scaladsl.{LocationService, LocationServiceFactory}
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}

import scala.concurrent.ExecutionContext

class Wiring(sequencerId: String, observingMode: String, port: Option[Int]) {
  lazy val clusterSettings = ClusterSettings()

  lazy implicit val system: ActorSystem                = clusterSettings.system
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService               = LocationServiceFactory.makeLocalHttpClient
  lazy val locationServiceWrapper: LocationServiceGateway = new LocationServiceGateway(locationService, system)
  lazy val eventService: EventService                     = new RedisEventServiceFactory().make(locationService)

  lazy val sequenceFeeder: SequenceFeeder = locationServiceWrapper.sequenceFeeder(sequencerId, observingMode)
  lazy val sequenceEditor: SequenceEditor = locationServiceWrapper.sequenceEditor(sequencerId, observingMode)

  lazy val configs   = new Configs(port)
  lazy val routes    = new Routes(sequenceFeeder, sequenceEditor, eventService, sequencerId, observingMode)
  lazy val rpcServer = new RpcServer(configs, routes)

}
