import ocs.framework.SequencerApp

object TestSequencerApp {
  def main(args: Array[String]): Unit = {
    val (sequencerId, observingMode) = args match {
      case Array(sId, oMode) => (sId, oMode)
      case _                 => throw new RuntimeException("please provide sequencerId, observationMode and ssh repl port parameters")
    }
    SequencerApp.run(sequencerId, observingMode)
  }
}
