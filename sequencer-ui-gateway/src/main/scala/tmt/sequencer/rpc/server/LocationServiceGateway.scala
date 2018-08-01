package tmt.sequencer.rpc.server

import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.{typed, ActorSystem, CoordinatedShutdown}
import csw.messages.location.Connection.AkkaConnection
import csw.messages.location.{AkkaLocation, ComponentId, ComponentType, Location}
import csw.messages.params.models.Prefix
import csw.services.location.commons.ActorSystemFactory
import csw.services.location.models.AkkaRegistration
import csw.services.location.scaladsl.LocationService
import csw.services.logging.messages.LogControlMessages
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.client.{SequenceEditorImpl, SequenceFeederImpl}
import tmt.sequencer.messages.SupervisorMsg

import scala.async.Async.async
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, ExecutionContext, Future}

class LocationServiceGateway(locationService: LocationService)(implicit ec: ExecutionContext, system: ActorSystem) {

  def resolve[T](componentName: String, componentType: ComponentType)(f: AkkaLocation => Future[T]): Future[T] =
    locationService
      .resolve(AkkaConnection(ComponentId(componentName, componentType)), 5.seconds)
      .flatMap {
        case Some(akkaLocation) =>
          f(akkaLocation)
        case None =>
          throw new IllegalArgumentException(s"Could not find component - $componentName of type - $componentType")
      }

  def sequenceFeeder(sequencerId: String, observingMode: String): Future[SequenceFeederImpl] = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederImpl(supervisorRef)
      }(system.dispatcher)
    }
  }

  def sequenceEditor(sequencerId: String, observingMode: String): Future[SequenceEditorImpl] = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceEditorImpl(supervisorRef)
      }(system.dispatcher)
    }
  }

  def list(componentType: ComponentType): Future[List[Location]] = locationService.list(componentType)
}
