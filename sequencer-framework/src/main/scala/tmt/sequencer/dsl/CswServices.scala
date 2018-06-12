package tmt.sequencer.dsl

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{KillSwitch, KillSwitches, Materializer, ThrottleMode}
import akka.util.Timeout
import akka.{util, Done}
import csw.messages.commands.Setup
import csw.messages.location.Connection.AkkaConnection
import csw.messages.location.{ComponentId, ComponentType}
import csw.services.command.scaladsl.CommandService
import csw.services.location.scaladsl.LocationService
import tmt.sequencer.api.SequenceFeeder
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.models.{AggregateResponse, Command, CommandResponse, SequencerEvent}
import tmt.sequencer.rpc.server.SequenceFeederImpl
import tmt.sequencer.util.{CswCommandAdapter, SequencerComponent}
import tmt.sequencer.{Engine, Sequencer}

import scala.async.Async.{async, await}
import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{Await, ExecutionContext, Future}

class CswServices(sequencer: Sequencer,
                  engine: Engine,
                  locationService: LocationService,
                  val sequencerId: String,
                  val observingMode: String)(implicit mat: Materializer, system: ActorSystem) {

  implicit val typedSystem                                                       = system.toTyped
  val commandHandlerBuilder: FunctionBuilder[Command, Future[AggregateResponse]] = new FunctionBuilder

  def handleCommand(name: String)(handler: Command => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler(_.commandName == name)(handler)
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

    Await.result(eventualFeederImpl, 7.seconds)
  }

  def nextIf(f: Command => Boolean): Future[Option[Command]] =
    async {
      val hasNext = await(sequencer.maybeNext).map(_.command).exists(f)
      if (hasNext) Some(await(sequencer.next).command) else None
    }(mat.executionContext)

  def setup(componentName: String, command: Command): Future[CommandResponse] =
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

      println(s"response ===> ${Await.result(eventualCommandResponse, 7.seconds)}")
      CommandResponse.Success(command.runId, s"Result submit: [$componentName] - $command")
    }(mat.executionContext)

  def subscribe(key: String)(callback: SequencerEvent => Done)(implicit strandEc: ExecutionContext): KillSwitch = {
    subscribeAsync(key)(e => Future(callback(e))(strandEc))
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => SequencerEvent)(implicit strandEc: ExecutionContext): Cancellable = {
    publishAsync(every)(Future(eventGeneratorBlock)(strandEc))
  }

  private def subscribeAsync(key: String)(callback: SequencerEvent => Future[Done]): KillSwitch = {
    Source
      .fromIterator(() => Iterator.from(1))
      .map(x => SequencerEvent(key, x.toString))
      .throttle(1, 20.second, 1, ThrottleMode.shaping)
      .mapAsync(1)(callback)
      .viaMat(KillSwitches.single)(Keep.right)
      .to(Sink.ignore)
      .run()
  }

  private def publishAsync(every: FiniteDuration)(eventGeneratorBlock: => Future[SequencerEvent]): Cancellable = {
    val source = Source.tick(0.millis, every, ()).mapAsync(1)(_ => eventGeneratorBlock)
    val sink = Sink.foreach[SequencerEvent] {
      case SequencerEvent(k, v) => println(s"published: event=$v on key=$k")
    }
    source.to(sink).run()
  }
}
