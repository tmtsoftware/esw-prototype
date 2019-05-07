package ocs.framework

import akka.Done
import akka.actor.typed.ActorRef
import akka.stream.Materializer
import csw.command.client.messages.CommandResponseManagerMessage
import csw.command.client.{CRMCacheProperties, CommandResponseManager, CommandResponseManagerActor}
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.event.client.models.EventStores.RedisStore
import csw.location.api.models.Connection.AkkaConnection
import csw.location.api.models.{AkkaLocation, ComponentId, ComponentType}
import csw.logging.client.scaladsl.LoggerFactory
import csw.params.core.models.Prefix
import csw.time.scheduler.TimeServiceSchedulerFactory
import csw.time.scheduler.api.TimeServiceScheduler
import io.lettuce.core.RedisClient
import ocs.api.client.{SequenceEditorJvmClient, SequencerCommandServiceJvmClient}
import ocs.api.messages.{SequencerMsg, SupervisorMsg}
import ocs.api.{SequenceEditor, SequencerCommandService, SequencerUtil}
import ocs.client.factory.ComponentFactory
import ocs.framework.core.{Engine, SequenceOperator, SequencerBehaviour, SupervisorBehavior}
import ocs.framework.dsl.{CswServices, Script}
import ocs.framework.util.ScriptLoader
import romaine.RomaineFactory

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

class Wiring(val sequencerId: String, val observingMode: String, cswSystem: CswSystem, redisClient: RedisClient) {

  import cswSystem._

  lazy implicit val mat: Materializer = createMaterializer()

  lazy val crmRef: ActorRef[CommandResponseManagerMessage] =
    cswSystem.userActorOf(CommandResponseManagerActor.behavior(CRMCacheProperties(), loggerFactory), "crm")

  lazy val commandResponseManager: CommandResponseManager = new CommandResponseManager(crmRef)

  lazy val sequencerRef: ActorRef[SequencerMsg] = cswSystem.userActorOf(SequencerBehaviour.behavior(crmRef), "sequencer")
  lazy val sequenceOperator                     = new SequenceOperator(sequencerRef, system)

  lazy val componentFactory = new ComponentFactory(locationServiceWrapper)

  lazy val eventService: EventService = new EventServiceFactory(RedisStore(redisClient)).make(locationService)
  lazy val configs                    = new Configs(sequencerId, observingMode)
  lazy val script: Script             = ScriptLoader.load(configs, cswServices)

  lazy val timeServiceScheduler: TimeServiceScheduler = TimeServiceSchedulerFactory.make()

  lazy val engine                         = new Engine
  lazy val romaineFactory: RomaineFactory = new RomaineFactory(redisClient)

  private val loggerFactory = new LoggerFactory("sequencer")

  lazy val componentId = ComponentId(SequencerUtil.getComponentName(sequencerId, observingMode), ComponentType.Sequencer)

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
    cswSystem.userActorOf(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor                   = new SequenceEditorJvmClient(supervisorRef)
  lazy val sequencerCommandService: SequencerCommandService = new SequencerCommandServiceJvmClient(supervisorRef)

  def shutDown(): Done = {
    Await.result(locationService.unregister(AkkaConnection(componentId)), 5.seconds)
    Await.result(script.shutdown(), 5.seconds)
    cswSystem.shutdownUserActors()
  }

  def start(): AkkaLocation = {
    //fixme: Logging actor can not be created with untyped system as Top level actor
    //LoggingSystemFactory.start("sample", "", "", system)
    engine.start(sequenceOperator, script)
    Await
      .result(locationServiceWrapper.register(Prefix("sequencer"), componentId, supervisorRef), 5.seconds)
      .location
      .asInstanceOf[AkkaLocation]
  }
}
