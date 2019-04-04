package ocs.framework

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import csw.command.client.messages.CommandResponseManagerMessage
import csw.command.client.{CRMCacheProperties, CommandResponseManager, CommandResponseManagerActor}
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.event.client.models.EventStores.RedisStore
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory
import csw.logging.client.scaladsl.LoggerFactory
import csw.time.scheduler.TimeServiceSchedulerFactory
import csw.time.scheduler.api.TimeServiceScheduler
import io.lettuce.core.RedisClient
import ocs.api.client.{SequenceEditorJvmClient, SequencerCommandServiceJvmClient}
import ocs.api.messages.{SequencerMsg, SupervisorMsg}
import ocs.api.{SequenceEditor, SequencerCommandService}
import ocs.client.factory.{ComponentFactory, LocationServiceWrapper}
import ocs.framework.core.{Engine, SequenceOperator, SequencerBehaviour, SupervisorBehavior}
import ocs.framework.dsl.{CswServices, Script}
import ocs.framework.util.ScriptLoader
import romaine.RomaineFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Wiring(sequencerId: String, observingMode: String, replPort: Int) {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy implicit val system: ActorSystem                     = ActorSystemFactory.remote()
  lazy implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped
  lazy implicit val materializer: Materializer              = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext      = system.dispatcher

  lazy val crmRef: ActorRef[CommandResponseManagerMessage] =
    system.spawn(CommandResponseManagerActor.behavior(CRMCacheProperties(), loggerFactory), "crm")
  lazy val commandResponseManager: CommandResponseManager = new CommandResponseManager(crmRef)

  lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior(crmRef), "sequencer")
  lazy val sequenceOperator                     = new SequenceOperator(sequencerRef, system)

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService, system)

  lazy val componentFactory = new ComponentFactory(locationServiceWrapper)

  lazy val redisClient: RedisClient   = RedisClient.create()
  lazy val eventService: EventService = new EventServiceFactory(RedisStore(redisClient)).make(locationService)
  lazy val configs                    = new Configs(sequencerId, observingMode, replPort)
  lazy val script: Script             = ScriptLoader.load(configs, cswServices)

  lazy val timeServiceScheduler: TimeServiceScheduler = TimeServiceSchedulerFactory.make()

  lazy val engine                         = new Engine
  lazy val romaineFactory: RomaineFactory = new RomaineFactory(redisClient)

  private val loggerFactory = new LoggerFactory("sequencer")

  lazy val cswServices =
    new CswServices(
      sequencerId,
      observingMode,
      sequenceOperator,
      componentFactory,
      locationServiceWrapper,
      eventService,
      timeServiceScheduler,
      romaineFactory,
      commandResponseManager
    )

  lazy val supervisorRef: ActorRef[SupervisorMsg] = system.spawn(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor                   = new SequenceEditorJvmClient(supervisorRef)
  lazy val sequencerCommandService: SequencerCommandService = new SequencerCommandServiceJvmClient(supervisorRef)

  lazy val remoteRepl =
    new RemoteRepl(cswServices, sequenceOperator, supervisorRef, sequencerCommandService, sequenceEditor, configs)
}
