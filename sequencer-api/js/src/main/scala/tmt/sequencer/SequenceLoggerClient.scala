package tmt.sequencer

import org.scalajs.dom.EventSource
import tmt.sequencer.api.SequenceLoggerWeb
import tmt.sequencer.models.WebRWSupport

import scala.concurrent.ExecutionContext

class SequenceLoggerClient(baseUri: String)(implicit ec: ExecutionContext) extends SequenceLoggerWeb with WebRWSupport {

  override def onLogEvent(callback: String => Unit): Unit = {
    val eventSource = new EventSource(s"$baseUri/sequencer/logs")
    eventSource.onmessage = { messageEvent =>
      callback(messageEvent.data.toString)
    }
  }
}
