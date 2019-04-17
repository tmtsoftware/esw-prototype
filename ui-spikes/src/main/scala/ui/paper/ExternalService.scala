package ui.paper

import com.raquo.airstream.signal.{StrictSignal, Var}
import ocs.api.{EventStream, WebGateway}
import play.api.libs.json.{JsObject, JsPath}

class ExternalService {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val defaultValue = Position(100, 100, 100)
  private val position     = Var(defaultValue)

  private val gateway = new WebGateway("https://stream.wikimedia.org")

  private val stream: EventStream[JsObject] = gateway.stream[JsObject]("/v2/stream/recentchange")

  def subscribe(): StrictSignal[Position] = {
    stream.onNext = { jsObject =>
      val _position = parse(jsObject)
      println(_position)
      position.set(_position)
    }
    position.signal
  }

  def parse(jsObject: JsObject): Position = {
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
