package ocs.framework

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory
import io.lettuce.core.RedisClient
import ocs.api.{SequenceEditor, SequenceFeeder}
import ocs.api.client.{SequenceEditorJvmClient, SequenceFeederJvmClient}
import ocs.api.messages.{SequencerMsg, SupervisorMsg}
import ocs.framework.dsl.{CswServices, Script}
import ocs.framework.util.{CommandServiceWrapper, LocationServiceGateway, ScriptLoader}
import romaine.RomaineFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Wiring(sequencerId: String, observingMode: String, replPort: Int) {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy implicit val system: ActorSystem                     = ActorSystemFactory.remote()
  lazy implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped
  lazy implicit val materializer: Materializer              = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext      = system.dispatcher

  lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
  lazy val sequencer                            = new Sequencer(sequencerRef, system)

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val locationServiceWrapper: LocationServiceGateway = new LocationServiceGateway(locationService, system)

  lazy val commandServiceWrapper = new CommandServiceWrapper(locationServiceWrapper)

  lazy val eventService: EventService = new EventServiceFactory().make(locationService)
  lazy val configs                    = new Configs(sequencerId, observingMode, replPort)
  lazy val script: Script             = ScriptLoader.load(configs, cswServices)
  lazy val engine                     = new Engine

  lazy val redisClient: RedisClient       = RedisClient.create()
  lazy val romaineFactory: RomaineFactory = new RomaineFactory(redisClient)

  lazy val cswServices =
    new CswServices(sequencerId,
                    observingMode,
                    sequencer,
                    locationServiceWrapper,
                    commandServiceWrapper,
                    eventService,
                    romaineFactory)

  lazy val supervisorRef: ActorRef[SupervisorMsg] = system.spawn(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor = new SequenceEditorJvmClient(supervisorRef)
  lazy val sequenceFeeder: SequenceFeeder = new SequenceFeederJvmClient(supervisorRef)

  lazy val remoteRepl = new RemoteRepl(cswServices, sequencer, supervisorRef, sequenceFeeder, sequenceEditor, configs)
}
