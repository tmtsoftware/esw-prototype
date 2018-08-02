package tmt.sequencer

import csw.messages.location.ComponentType
import csw.services.logging.scaladsl.LoggingSystemFactory
import tmt.sequencer.util.SequencerComponent

object SequencerApp {
  def run(sequencerId: String, observingMode: String, replPort: Int): Unit = {
    val wiring = new Wiring(sequencerId, observingMode, replPort)
    import wiring._

    LoggingSystemFactory.start("sample", "", "", system)
    engine.start(sequencer, script)

    locationServiceWrapper.register(SequencerComponent.getComponentName(sequencerId, observingMode),
                                    ComponentType.Sequencer,
                                    supervisorRef)

    Thread.sleep(4000)
    remoteRepl.server().start()
  }
}
