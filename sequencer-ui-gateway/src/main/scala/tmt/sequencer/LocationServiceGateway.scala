package tmt.sequencer

import akka.actor.ActorSystem
import csw.messages.location.Connection.AkkaConnection
import csw.messages.location.{AkkaLocation, ComponentId, ComponentType, Location}
import csw.services.location.scaladsl.LocationService
import tmt.sequencer.client.{SequenceEditorClient, SequenceFeederClient}
import tmt.sequencer.messages.SupervisorMsg

import scala.async.Async.async
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContext, Future}

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

  def sequenceFeeder(sequencerId: String, observingMode: String): Future[SequenceFeederClient] = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederClient(supervisorRef)
      }(system.dispatcher)
    }
  }

  def sequenceEditor(sequencerId: String, observingMode: String): Future[SequenceEditorClient] = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceEditorClient(supervisorRef)
      }(system.dispatcher)
    }
  }

  def list(componentType: ComponentType): Future[List[Location]] = locationService.list(componentType)
}
