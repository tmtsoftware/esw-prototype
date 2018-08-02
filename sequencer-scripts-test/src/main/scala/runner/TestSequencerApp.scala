package runner

import tmt.sequencer.SequencerApp

object TestSequencerApp {
  def main(args: Array[String]): Unit = {
    val (sequencerId, observingMode, replPort) = args match {
      case Array(sId, oMode, p) => (sId, oMode, p.toInt)
      case _                    => throw new RuntimeException("please provide sequencerId, observationMode and ssh repl port parameters")
    }
    SequencerApp.run(sequencerId, observingMode, replPort)
  }
}
