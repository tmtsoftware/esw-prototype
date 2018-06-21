package tmt.sequencer.dsl

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{typed, ActorSystem, Cancellable}
import akka.stream.Materializer
import akka.util.Timeout
import akka.{util, Done}
import csw.messages.commands.{SequenceCommand, Setup}
import csw.messages.events.{Event, EventKey}
import csw.messages.location.ComponentType
import csw.services.command.scaladsl.CommandService
import csw.services.event.scaladsl.{EventService, EventSubscription}
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.models.CommandResponse
import tmt.sequencer.rpc.server.SequenceFeederImpl
import tmt.sequencer.util._
import tmt.sequencer.{Engine, Sequencer}

import scala.async.Async.{async, await}
import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{Await, ExecutionContext, Future}

class CswServices(sequencer: Sequencer,
                  engine: Engine,
                  locationService: LocationServiceWrapper,
                  eventService: EventService,
                  val sequencerId: String,
                  val observingMode: String)(implicit mat: Materializer, system: ActorSystem)
    extends CommandDsl(sequencer)(system.dispatcher) {

  implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  def sequenceFeeder(subSystemSequencerId: String): SequenceFeederImpl = {
    val componentName = SequencerComponent.getComponentName(subSystemSequencerId, observingMode)
    val eventualFeederImpl = locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederImpl(supervisorRef)
      }(mat.executionContext)
    }
    Await.result(eventualFeederImpl, 5.seconds)
  }

  def setup(assemblyName: String, command: SequenceCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        val setupCommand: Setup       = CswCommandAdapter.setupCommandFrom(command)
        implicit val timeout: Timeout = util.Timeout(5.seconds)
        val eventualCommandResponse   = new CommandService(akkaLocation).submit(setupCommand)
        val response                  = await(eventualCommandResponse)
        println(s"Response - $response")
        CommandResponse.Success(command.runId, s"Result submit: [$assemblyName] - $command")
      }(mat.executionContext)
    }
  }

  def subscribe(eventKeys: Set[EventKey])(callback: Event => Done)(implicit strandEc: ExecutionContext): SubscriptionStream = {
    println(s"==========================> Subscribing event $eventKeys")
    val eventualSubscription: Future[EventSubscription] =
      eventService.defaultSubscriber.map(_.subscribeAsync(eventKeys, e => Future(callback(e))))(strandEc)
    SubscriptionStream(eventualSubscription)
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => Event)(implicit strandEc: ExecutionContext): PublisherStream = {
    println(s"=========================> Publishing event $eventGeneratorBlock every $every")
    val eventualCancellable: Future[Cancellable] =
      eventService.defaultPublisher.map(_.publish(eventGeneratorBlock, every))(strandEc)
    PublisherStream(eventualCancellable)
  }
}
