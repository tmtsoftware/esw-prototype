package ocs.framework.wrapper

import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import csw.command.api.scaladsl.CommandService
import csw.command.client._
import csw.location.api.models.ComponentType
import ocs.api.client.{SequenceEditorJvmClient, SequenceFeederJvmClient}
import ocs.api.messages.SupervisorMsg
import ocs.api.{SequenceEditor, SequenceFeeder, SequencerUtil}

import scala.concurrent.Future

class ComponentFactory(locationService: LocationServiceWrapper)(implicit system: ActorSystem) {

  implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  def assembly(assemblyName: String): Future[CommandService] = {
    locationService.resolve(assemblyName, ComponentType.Assembly)(akkaLocation => CommandServiceFactory.make(akkaLocation))
  }

  def sequenceFeeder(sequencerId: String, observingMode: String): Future[SequenceFeeder] = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
      new SequenceFeederJvmClient(supervisorRef)
    }
  }

  def sequenceEditor(subSystemSequencerId: String, observingMode: String): Future[SequenceEditor] = {
    val componentName = SequencerUtil.getComponentName(subSystemSequencerId, observingMode)
    locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
      new SequenceEditorJvmClient(supervisorRef)
    }
  }
}
