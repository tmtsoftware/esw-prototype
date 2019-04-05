package ocs.framework

import akka.Done
import akka.actor.CoordinatedShutdown
import csw.logging.client.scaladsl.LoggingSystemFactory
import csw.params.core.models.Prefix
import ocs.api.SequencerUtil

import scala.concurrent.Future

object SequencerApp {
  def run(sequencerId: String, observingMode: String): Unit = {
    val wiring = new Wiring(sequencerId, observingMode)
    import wiring._

    LoggingSystemFactory.start("sample", "", "", system)
    engine.start(sequenceOperator, script)

    locationServiceWrapper.registerSequencer(
      Prefix("sequencer"),
      SequencerUtil.getComponentName(sequencerId, observingMode),
      supervisorRef
    )

    CoordinatedShutdown(system).addTask(
      CoordinatedShutdown.PhaseBeforeServiceUnbind,
      "Shutdown redis client"
    ) { () =>
      println("Shutting down redis client")
      Future(redisClient.shutdown()).map(_ => Done)
    }

  }
}
