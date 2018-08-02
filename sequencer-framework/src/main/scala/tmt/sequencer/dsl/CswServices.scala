package tmt.sequencer.dsl

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{typed, ActorSystem, Cancellable}
import akka.util.Timeout
import akka.{util, Done}
import csw.messages.commands.{CommandResponse, SequenceCommand, Setup}
import csw.messages.events.{Event, EventKey}
import csw.messages.location.ComponentType
import csw.services.command.scaladsl.CommandService
import csw.services.event.api.scaladsl.{EventService, EventSubscription}
import org.tmt.macros.StrandEc
import tmt.sequencer.api.SequenceFeeder
import tmt.sequencer.client.SequenceFeederClient
import tmt.sequencer.messages.SupervisorMsg
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

  implicit val strandEc: StrandEc = StrandEc.create()

  def sequenceFeeder(subSystemSequencerId: String): SequenceFeeder = {
    val componentName = SequencerComponent.getComponentName(subSystemSequencerId, observingMode)
    val eventualFeederImpl = locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederClient(supervisorRef)
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
        response
      }(system.dispatcher)
    }
  }

  def subscribe(eventKeys: Set[EventKey])(callback: Event => Done): SubscriptionStream = {
    println(s"==========================> Subscribing event $eventKeys")
    val eventualSubscription: Future[EventSubscription] = spawn {
      eventService.defaultSubscriber.await.subscribeAsync(eventKeys, e => spawn(callback(e)))
    }
    new SubscriptionStream(eventualSubscription)
  }

  def publish(every: FiniteDuration)(eventGeneratorBlock: => Event): PublisherStream = {
    println(s"=========================> Publishing event $eventGeneratorBlock every $every")
    val eventualCancellable: Future[Cancellable] = spawn {
      eventService.defaultPublisher.await.publish(eventGeneratorBlock, every)
    }
    new PublisherStream(eventualCancellable)
  }

  def sendResult(msg: String): Unit = spawn {
    eventService.defaultPublisher.await.publish(ResultEvent.createResultEvent(s"$sequencerId-$observingMode", msg)).await
  }
}
