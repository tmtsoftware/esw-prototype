package runner

import tmt.sequencer.SequencerApp

object TestSequencerApp {
  def main(args: Array[String]): Unit = {
    val (sequencerId, observingMode, port) = args match {
      case Array(sId, oMode, p) => (sId, oMode, Some(p.toInt))
      case Array(sId, oMode)    => (sId, oMode, None)
      case _                    => throw new RuntimeException("please provide both sequencerId and observationMode parameters")
    }
    SequencerApp.run(sequencerId, observingMode, port)
  }
}
