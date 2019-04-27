package ui.mirror

import ocs.api.{EventStream, WebGateway}
import play.api.libs.json.{JsObject, JsPath}

class ExternalService(store: Store) {
  import scala.concurrent.ExecutionContext.Implicits.global

  private lazy val gateway = new WebGateway("https://stream.wikimedia.org")

  def subscribe(): Unit = {
    val eventStream: EventStream[JsObject] = gateway.stream[JsObject]("/v2/stream/recentchange")
    eventStream.onNext = { jsObject =>
      val _position = parse(jsObject)
//      println(_position)
      _position.foreach(store.faultPositions.set)
    }
  }

  private def parse(jsObject: JsObject): Option[Position] = {
    (JsPath \ "revision" \ "new")
      .asSingleJsResult(jsObject)
      .asOpt
      .map { jsValue =>
        val numbers = jsValue.as[Int].toString.take(3).map(x => x.toString.toInt)
        Position(numbers(0), numbers(1), numbers(2))
      }
  }

}
