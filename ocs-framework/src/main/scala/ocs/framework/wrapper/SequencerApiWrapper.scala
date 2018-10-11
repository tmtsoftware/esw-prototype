package ocs.framework.wrapper
import akka.actor.ActorSystem
import csw.location.api.models.ComponentType
import ocs.api.client.{SequenceEditorJvmClient, SequenceFeederJvmClient}
import ocs.api.messages.SupervisorMsg
import ocs.api.{SequenceEditor, SequenceFeeder, SequencerUtil}

import scala.async.Async.async
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class SequencerApiWrapper(locationService: LocationServiceWrapper)(implicit system: ActorSystem) {

  def sequenceFeeder(sequencerId: String, observingMode: String): SequenceFeeder = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    val eventualFeederImpl = locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederJvmClient(supervisorRef)
      }(system.dispatcher)
    }
    Await.result(eventualFeederImpl, 5.seconds)
  }

  def sequenceEditor(subSystemSequencerId: String, observingMode: String): SequenceEditor = {
    val componentName = SequencerUtil.getComponentName(subSystemSequencerId, observingMode)
    val eventualEditorImpl = locationService.resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceEditorJvmClient(supervisorRef)
      }(system.dispatcher)
    }
    Await.result(eventualEditorImpl, 5.seconds)
  }
}
