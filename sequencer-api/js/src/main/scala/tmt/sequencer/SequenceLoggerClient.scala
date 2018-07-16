package tmt.sequencer

import org.scalajs.dom.EventSource
import tmt.sequencer.api.SequenceLoggerWeb
import tmt.sequencer.models.WebRWSupport

class SequenceLoggerClient(baseUri: String) extends SequenceLoggerWeb with WebRWSupport {
  val eventSource = new EventSource(s"$baseUri/${SequenceLoggerWeb.ApiName}/${SequenceLoggerWeb.logs}")

  override def onLogEvent(callback: String => Unit): Unit = {
    eventSource.onmessage = { messageEvent =>
      callback(messageEvent.data.toString)
    }
  }

  def closeEventSource(): Unit = {
    eventSource.close()
  }
}
