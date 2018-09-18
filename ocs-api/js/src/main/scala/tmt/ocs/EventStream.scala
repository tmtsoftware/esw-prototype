package tmt.ocs

import org.scalajs.dom.EventSource
import play.api.libs.json.{Json, Reads}

class EventStream[T: Reads](eventSource: EventSource) {

  var onNext: T => Unit = { _ =>
    ()
  }

  eventSource.onmessage = { messageEvent =>
    val data = messageEvent.data.toString
    if (data.nonEmpty) {
      println(s"**********$data")
      val event = Json.parse(data).as[T]
      onNext(event)
    }
  }

  def close(): Unit = eventSource.close()
}
