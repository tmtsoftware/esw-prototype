package tmt.sequencer.api

trait SequenceLoggerWeb {
  def onLogEvent(callback: String => Unit): Unit
}
