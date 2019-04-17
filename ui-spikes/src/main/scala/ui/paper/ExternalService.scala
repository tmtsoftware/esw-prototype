package ui.paper

import com.raquo.airstream.eventstream
import com.raquo.airstream.signal.Var
import ocs.api.{EventStream, WebGateway}
import play.api.libs.json.{JsObject, JsPath}

class ExternalService {
  import scala.concurrent.ExecutionContext.Implicits.global

  subscribe()
  def positions: eventstream.EventStream[Position] = position.signal.changes.delay(1000)

  private lazy val defaultValue = Position(100, 100, 100)
  private lazy val position     = Var(defaultValue)

  private lazy val gateway = new WebGateway("https://stream.wikimedia.org")

  private def subscribe(): Unit = {
    val eventStream: EventStream[JsObject] = gateway.stream[JsObject]("/v2/stream/recentchange")
    eventStream.onNext = { jsObject =>
      val _position = parse(jsObject)
      println(_position)
      position.set(_position)
    }
  }

  private def parse(jsObject: JsObject): Position = {
    (JsPath \ "revision" \ "new")
      .asSingleJsResult(jsObject)
      .asOpt
      .map { jsValue =>
        val numbers = jsValue.as[Int].toString.take(3).map(x => x.toString.toInt)
        Position(numbers(0), numbers(1), numbers(2))
      }
      .getOrElse(defaultValue)
  }

}
