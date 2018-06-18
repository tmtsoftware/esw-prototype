package tmt.sequencer.dsl

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{typed, ActorSystem, Cancellable}
import akka.stream.Materializer
import akka.util.Timeout
import akka.{util, Done}
import csw.messages.commands.{SequenceCommand, Setup}
import csw.messages.events.{Event, EventKey}
import csw.messages.location.Connection.AkkaConnection
import csw.messages.location.{ComponentId, ComponentType}
import csw.services.command.scaladsl.CommandService
import csw.services.event.scaladsl.{EventService, EventSubscription}
import csw.services.location.scaladsl.LocationService
import tmt.sequencer.api.SequenceFeeder
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.models.{AggregateResponse, CommandResponse}
import tmt.sequencer.rpc.server.SequenceFeederImpl
import tmt.sequencer.util.{CswCommandAdapter, SequencerComponent}
import tmt.sequencer.{Engine, Sequencer}

import scala.async.Async.{async, await}
import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{Await, ExecutionContext, Future}

class CswServices(sequencer: Sequencer,
                  engine: Engine,
                  locationService: LocationService,
                  eventService: EventService,
                  val sequencerId: String,
                  val observingMode: String)(implicit mat: Materializer, system: ActorSystem) {

  implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[AggregateResponse]] = new FunctionBuilder

  def handleCommand(name: String)(handler: SequenceCommand => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler(_.commandName.name == name)(handler)
  }

  def sequenceProcessor(sequencerId: String): SequenceFeeder = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    val componentId   = ComponentId(componentName, ComponentType.Sequencer)
    val connection    = AkkaConnection(componentId)
    val eventualFeederImpl = locationService
      .resolve(connection, 5.seconds)
      .map {
        case Some(akkaLocation) =>
          val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
          new SequenceFeederImpl(supervisorRef)
        case None =>
          throw new IllegalArgumentException(s"sequencer = $sequencerId with observing mode = $observingMode not found")
      }(mat.executionContext)

    println("*****************************************")
    val feederImpl = Await.result(eventualFeederImpl, 7.seconds)
    println("*****************************************" + feederImpl)
    feederImpl
  }

  def nextIf(f: SequenceCommand => Boolean): Future[Option[SequenceCommand]] =
    async {
      val hasNext = await(sequencer.maybeNext).map(_.command).exists(f)
      if (hasNext) Some(await(sequencer.next).command) else None
    }(mat.executionContext)

  def setup(componentName: String, command: SequenceCommand): Future[CommandResponse] =
    async {
      val componentId = ComponentId(componentName, ComponentType.Assembly)
      val connection  = AkkaConnection(componentId)
      val eventualCommandResponse = locationService
        .resolve(connection, 5.seconds)
        .flatMap {
          case Some(akkaLocation) =>
            val setupCommand: Setup       = CswCommandAdapter.setupCommandFrom(command)
            implicit val timeout: Timeout = util.Timeout(5.seconds)

            new CommandService(akkaLocation).submit(setupCommand)
          case None =>
            throw new IllegalArgumentException(s"sequencer = $sequencerId with observing mode = $observingMode not found")
        }(mat.executionContext)
      val response = Await.result(eventualCommandResponse, 7.seconds)
      println(s"response ===> $response")
      CommandResponse.Success(command.runId, s"Result submit: [$componentName] - $command")
    }(mat.executionContext)

  def subscribe(eventKeys: Set[EventKey])(callback: Event => Done)(implicit strandEc: ExecutionContext): EventSubscription = {
    println(s"==========================> Subscribing event $eventKeys")
    val eventSubscriber = eventService.defaultSubscriber.map(_.subscribeAsync(eventKeys, e => Future(callback(e))))(strandEc)
    Await.result(eventSubscriber, 2.seconds)
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => Event)(implicit strandEc: ExecutionContext): Cancellable = {
    println(s"=========================> Publishing event $eventGeneratorBlock every $every")
    val eventPublisher: Future[Cancellable] = eventService.defaultPublisher.map(_.publish(eventGeneratorBlock, every))(strandEc)
    Await.result(eventPublisher, every)
  }
}
