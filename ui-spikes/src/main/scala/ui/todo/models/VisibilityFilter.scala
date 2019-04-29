package ui.todo.models

import scala.scalajs.js

abstract class VisibilityFilter(val value: String) extends js.Object

object VisibilityFilter {
  object All       extends VisibilityFilter("All")
  object Completed extends VisibilityFilter("Completed")
  object Active    extends VisibilityFilter("Active")
}
