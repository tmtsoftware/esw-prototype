package ui.config.models

import play.api.libs.json.{Json, OFormat}

case class Item(path: String, id: String, author: String, comment: String)

case object Item {
  implicit val format: OFormat[Item] = Json.format[Item]
}
