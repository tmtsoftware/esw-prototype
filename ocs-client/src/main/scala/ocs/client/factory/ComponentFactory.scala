package ocs.client.factory

import akka.actor.typed.ActorSystem
import csw.command.api.scaladsl.CommandService
import csw.command.client._
import csw.location.api.models.{AkkaLocation, ComponentType}
import ocs.api.client.{SequenceComponentJvmClient, SequenceEditorJvmClient, SequencerCommandServiceJvmClient}
import ocs.api.messages.{SequenceComponentMsg, SupervisorMsg}
import ocs.api.{SequenceEditor, SequencerCommandService, SequencerUtil}

import scala.concurrent.Future

class ComponentFactory(locationService: LocationServiceWrapper)(implicit typedSystem: ActorSystem[_]) {

  def assemblyCommandService(assemblyName: String): Future[CommandService] = {
    locationService.resolve(assemblyName, ComponentType.Assembly)(akkaLocation => CommandServiceFactory.make(akkaLocation))
  }

  def hcdCommandService(hcdName: String): Future[CommandService] = {
    locationService.resolve(hcdName, ComponentType.HCD)(akkaLocation => CommandServiceFactory.make(akkaLocation))
  }

  def assemblyLocation(assemblyName: String): Future[AkkaLocation] = {
    locationService.resolve(assemblyName, ComponentType.Assembly)(identity)
  }

  def sequenceComponentService(sequenceComponentName: String): Future[SequenceComponentJvmClient] = {
    locationService.resolve(sequenceComponentName, ComponentType.Service) { akkaLocation =>
      val sequenceComponentRef = akkaLocation.actorRef.unsafeUpcast[SequenceComponentMsg]
      new SequenceComponentJvmClient(sequenceComponentRef, typedSystem)
    }
  }

  def sequenceCommandService(sequencerId: String, observingMode: String): Future[SequencerCommandService] = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      val supervisorRef = akkaLocation.actorRef.unsafeUpcast[SupervisorMsg]
      new SequencerCommandServiceJvmClient(supervisorRef)
    }
  }

  def sequenceEditor(subSystemSequencerId: String, observingMode: String): Future[SequenceEditor] = {
    val componentName = SequencerUtil.getComponentName(subSystemSequencerId, observingMode)
    locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      val supervisorRef = akkaLocation.actorRef.unsafeUpcast[SupervisorMsg]
      new SequenceEditorJvmClient(supervisorRef)
    }
  }
}
