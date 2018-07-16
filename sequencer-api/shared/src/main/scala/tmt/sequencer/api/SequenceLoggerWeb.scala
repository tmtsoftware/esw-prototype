package tmt.sequencer.api

trait SequenceLoggerWeb {
  def onLogEvent(callback: String => Unit): Unit
}

object SequenceLoggerWeb {
  val ApiName = "sequencer"
  val logs    = "logs"
}
