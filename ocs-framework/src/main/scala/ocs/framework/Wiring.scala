package ocs.framework

import akka.actor.typed.ActorRef
import csw.command.client.messages.CommandResponseManagerMessage
import csw.command.client.{CRMCacheProperties, CommandResponseManager, CommandResponseManagerActor}
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.event.client.models.EventStores.RedisStore
import csw.location.api.scaladsl.LocationService
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

class Wiring(sequencerId: String, observingMode: String, cswSystem: CswSystem, redisClient: RedisClient) {

  import cswSystem._

  lazy val crmRef: ActorRef[CommandResponseManagerMessage] =
    cswSystem.spawn(CommandResponseManagerActor.behavior(CRMCacheProperties(), loggerFactory), "crm")

  lazy val commandResponseManager: CommandResponseManager = new CommandResponseManager(crmRef)

  lazy val sequencerRef: ActorRef[SequencerMsg] = cswSystem.spawn(SequencerBehaviour.behavior(crmRef), "sequencer")
  lazy val sequenceOperator                     = new SequenceOperator(sequencerRef, system)

  lazy val locationService: LocationService               = HttpLocationServiceFactory.makeLocalClient
  lazy val locationServiceWrapper: LocationServiceWrapper = new LocationServiceWrapper(locationService, system)

  lazy val componentFactory = new ComponentFactory(locationServiceWrapper)

  lazy val eventService: EventService = new EventServiceFactory(RedisStore(redisClient)).make(locationService)
  lazy val configs                    = new Configs(sequencerId, observingMode)
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

  lazy val supervisorRef: ActorRef[SupervisorMsg] =
    cswSystem.spawn(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor                   = new SequenceEditorJvmClient(supervisorRef)
  lazy val sequencerCommandService: SequencerCommandService = new SequencerCommandServiceJvmClient(supervisorRef)
}
