package tmt.ocs

import csw.location.api.models.ComponentType
import csw.logging.scaladsl.LoggingSystemFactory

import scala.concurrent.Future

object SequencerApp {
  def run(sequencerId: String, observingMode: String, replPort: Int): Future[Unit] = {
    val wiring = new Wiring(sequencerId, observingMode, replPort)
    import wiring._

    LoggingSystemFactory.start("sample", "", "", system)
    engine.start(sequencer, script)

    locationServiceWrapper
      .register(
        SequencerUtil.getComponentName(sequencerId, observingMode),
        ComponentType.Sequencer,
        supervisorRef
      )
      .map { _ =>
        remoteRepl.server().start()
      }
  }
}
