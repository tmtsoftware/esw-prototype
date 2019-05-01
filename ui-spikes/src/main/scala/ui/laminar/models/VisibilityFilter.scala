package ui.laminar.models

sealed trait VisibilityFilter
object VisibilityFilter {
  case object All       extends VisibilityFilter
  case object Completed extends VisibilityFilter
  case object Active    extends VisibilityFilter
}
