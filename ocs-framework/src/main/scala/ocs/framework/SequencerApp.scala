package ocs.framework

import csw.logging.scaladsl.LoggingSystemFactory
import csw.params.core.models.Prefix
import ocs.api.SequencerUtil

object SequencerApp {
  def run(sequencerId: String, observingMode: String, replPort: Int): Unit = {
    val wiring = new Wiring(sequencerId, observingMode, replPort)
    import wiring._

    LoggingSystemFactory.start("sample", "", "", system)
    engine.start(sequencer, script)

    locationServiceWrapper.registerSequencer(
      Prefix("sequencer"),
      SequencerUtil.getComponentName(sequencerId, observingMode),
      supervisorRef
    )

    Thread.sleep(4000)
    remoteRepl.server().start()
  }
}
