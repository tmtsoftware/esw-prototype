package tmt.sequencer.models

import play.api.libs.json._
import ujson.Js
import upickle.default.{ReadWriter => RW, _}

object UpickleFormatAdapter {
  val jsValueRW: RW[JsValue] = readwriter[Js.Value].bimap(
    x => read[Js.Value](x.toString()),
    x => Json.parse(x.toString())
  )

  def playJsonToUpickle[T](implicit format: Format[T]): RW[T] = jsValueRW.bimap[T](
    result => Json.toJson(result),
    jsValue => jsValue.as[T]
  )
}
