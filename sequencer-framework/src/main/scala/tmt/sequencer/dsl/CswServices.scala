package tmt.sequencer.dsl

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{typed, ActorSystem, Cancellable}
import akka.util.Timeout
import akka.{util, Done}
import csw.messages.commands.{SequenceCommand, Setup}
import csw.messages.events.{Event, EventKey}
import csw.messages.location.ComponentType
import csw.services.command.scaladsl.CommandService
import csw.services.event.scaladsl.{EventService, EventSubscription}
import org.tmt.macros.StrandEc
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.models.CommandResponse
import tmt.sequencer.rpc.server.SequenceFeederImpl
import tmt.sequencer.util._
import tmt.sequencer.{Engine, Sequencer}

import scala.async.Async._
import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{Await, Future}

class CswServices(sequencer: Sequencer,
                  engine: Engine,
                  locationService: LocationServiceGateway,
                  eventService: EventService,
                  val sequencerId: String,
                  val observingMode: String)(implicit system: ActorSystem)
    extends CommandDsl(sequencer) {

  implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  def sequenceFeeder(subSystemSequencerId: String): SequenceFeederImpl = {
    val componentName = SequencerComponent.getComponentName(subSystemSequencerId, observingMode)
    val eventualFeederImpl = locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederImpl(supervisorRef)
      }(system.dispatcher)
    }
    Await.result(eventualFeederImpl, 5.seconds)
  }

  def setup(assemblyName: String, command: SequenceCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        val setupCommand: Setup       = CswCommandAdapter.setupCommandFrom(command)
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).submit(setupCommand))
        println(s"Response - $response")
        CommandResponse.Success(command.runId, s"Result submit: [$assemblyName] - $command")
      }(system.dispatcher)
    }
  }

  def subscribe(eventKeys: Set[EventKey])(callback: Event => Done)(implicit strandEc: StrandEc): SubscriptionStream = {
    println(s"==========================> Subscribing event $eventKeys")
    val eventualSubscription: Future[EventSubscription] = spawn {
      eventService.defaultSubscriber.await.subscribeAsync(eventKeys, e => spawn(callback(e)))
    }
    new SubscriptionStream(eventualSubscription)
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => Event)(implicit strandEc: StrandEc): PublisherStream = {
    println(s"=========================> Publishing event $eventGeneratorBlock every $every")
    val eventualCancellable: Future[Cancellable] = spawn {
      eventService.defaultPublisher.await.publish(eventGeneratorBlock, every)
    }
    new PublisherStream(eventualCancellable)
  }
}
