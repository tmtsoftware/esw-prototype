package tmt.sequencer.models

import play.api.libs.json._
import upickle.default.{ReadWriter => RW, _}

object UpickleFormatAdapter {
  def playJsonToUpickle[T](implicit format: Format[T]): RW[T] =
    readwriter[String]
      .bimap[T](
        result => format.writes(result).toString(),
        str => format.reads(Json.parse(str)).get
      )

  def upickleToPlayJson[T](implicit rw: RW[T]): Format[T] = new Format[T] {
    override def reads(json: JsValue): JsResult[T] = JsSuccess(read[T](json.toString()))
    override def writes(o: T): JsValue             = Json.parse(write(o))
  }
}
