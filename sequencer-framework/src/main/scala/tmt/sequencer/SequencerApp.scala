package tmt.sequencer

import akka.actor.CoordinatedShutdown
import csw.messages.location.Connection.AkkaConnection
import csw.messages.location.{ComponentId, ComponentType}
import csw.services.location.models.AkkaRegistration
import tmt.sequencer.util.SequencerComponent

object SequencerApp {
  def run(sequencerId: String, observingMode: String, port: Option[Int]): Unit = {
    val wiring = new Wiring(sequencerId, observingMode, port)
    import wiring._
    engine.start(sequencer, script)
    rpcServer.start()
    val componentId = ComponentId(SequencerComponent.getComponentName(sequencerId, observingMode), ComponentType.Sequencer)
    val connection  = AkkaConnection(componentId)

    val registration = AkkaRegistration(connection, Some("sequencer"), supervisorRef, null)
    locationService.register(registration).map { registrationResult =>
      println(s"Successfully registered $sequencerId with $observingMode - $registrationResult")

      coordinatedShutdown.addTask(
        CoordinatedShutdown.PhaseBeforeServiceUnbind,
        s"unregistering-${registrationResult.location}"
      ) { () =>
        println(s"Shutting down actor system, unregistering-${registrationResult.location}")
        registrationResult.unregister()
      }
    }

    Thread.sleep(4000)
    remoteRepl.server().start()
  }
}
