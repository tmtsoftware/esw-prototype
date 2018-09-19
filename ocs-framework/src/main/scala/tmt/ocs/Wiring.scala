package tmt.ocs

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.location.api.scaladsl.LocationService
import csw.location.commons.ClusterSettings
import csw.location.scaladsl.LocationServiceFactory
import io.lettuce.core.RedisClient
import romaine.RomaineFactory
import tmt.ocs.api.{SequenceEditor, SequenceFeeder}
import tmt.ocs.client.{SequenceEditorJvmClient, SequenceFeederJvmClient}
import tmt.ocs.dsl.{CswServices, Script}
import tmt.ocs.messages.{SequencerMsg, SupervisorMsg}
import tmt.ocs.util.{LocationServiceGateway, ScriptLoader}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Wiring(sequencerId: String, observingMode: String, replPort: Int) {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)
  lazy val clusterSettings           = ClusterSettings()

  lazy implicit val system: ActorSystem                     = clusterSettings.system
  lazy implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped
  lazy implicit val materializer: Materializer              = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext      = system.dispatcher

  lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
  lazy val sequencer                            = new Sequencer(sequencerRef, system)

  lazy val locationService: LocationService               = LocationServiceFactory.makeLocalHttpClient
  lazy val locationServiceWrapper: LocationServiceGateway = new LocationServiceGateway(locationService, system)

  lazy val eventService: EventService = new EventServiceFactory().make(locationService)
  lazy val configs                    = new Configs(sequencerId, observingMode, replPort)
  lazy val script: Script             = ScriptLoader.load(configs, cswServices)
  lazy val engine                     = new Engine

  lazy val redisClient: RedisClient       = RedisClient.create()
  lazy val romaineFactory: RomaineFactory = new RomaineFactory(redisClient)

  lazy val cswServices =
    new CswServices(sequencerId, observingMode, sequencer, locationServiceWrapper, eventService, romaineFactory)

  lazy val supervisorRef: ActorRef[SupervisorMsg] = system.spawn(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor = new SequenceEditorJvmClient(supervisorRef)
  lazy val sequenceFeeder: SequenceFeeder = new SequenceFeederJvmClient(supervisorRef)

  lazy val remoteRepl = new RemoteRepl(cswServices, sequencer, supervisorRef, sequenceFeeder, sequenceEditor, configs)
}
