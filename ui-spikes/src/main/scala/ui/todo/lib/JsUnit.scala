package ui.todo.lib

import scala.language.implicitConversions
import scala.scalajs.js

final class JsUnit private extends js.Object

object JsUnit {
  implicit def from(x: Unit): JsUnit = new JsUnit
}
