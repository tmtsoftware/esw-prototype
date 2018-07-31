package tmt.sequencer

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import csw.services.event.api.scaladsl.EventService
import csw.services.event.internal.redis.RedisEventServiceFactory
import csw.services.location.commons.ClusterSettings
import csw.services.location.scaladsl.{LocationService, LocationServiceFactory}
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.dsl.{CswServices, Script}
import tmt.sequencer.messages.{SequencerMsg, SupervisorMsg}
import tmt.sequencer.rpc.server._
import tmt.sequencer.scripts.ScriptLoader
import tmt.sequencer.util.LocationServiceGateway

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Wiring(sequencerId: String, observingMode: String, port: Option[Int]) {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)
  lazy val clusterSettings           = ClusterSettings()

  lazy implicit val system: ActorSystem                     = clusterSettings.system
  lazy implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped
  lazy implicit val materializer: Materializer              = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext      = system.dispatcher

  lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
  lazy val sequencer                            = new Sequencer(sequencerRef, system)

//  lazy val locationService: LocationService               = LocationServiceFactory.withSystem(system)
  lazy val locationService: LocationService               = LocationServiceFactory.makeLocalHttpClient
  lazy val locationServiceWrapper: LocationServiceGateway = new LocationServiceGateway(locationService, system)

  lazy val eventService: EventService = new RedisEventServiceFactory().make(locationService)
  lazy val configs                    = new Configs(sequencerId, observingMode, port)
  lazy val script: Script             = ScriptLoader.load(configs, cswServices)
  lazy val engine                     = new Engine
  lazy val cswServices                = new CswServices(sequencer, engine, locationServiceWrapper, eventService, sequencerId, observingMode)

  lazy val supervisorRef: ActorRef[SupervisorMsg] = system.spawn(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor = new SequenceEditorImpl(supervisorRef)
  lazy val sequenceFeeder: SequenceFeeder = new SequenceFeederImpl(supervisorRef)
  lazy val routes                         = new Routes(sequenceFeeder, sequenceEditor, eventService, sequencerId, observingMode)
  lazy val rpcServer                      = new RpcServer(configs, routes)

  lazy val remoteRepl = new RemoteRepl(cswServices, sequencer, supervisorRef, sequenceFeeder, sequenceEditor, configs)
}
