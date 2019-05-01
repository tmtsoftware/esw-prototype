package ui.laminar.context
import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.{Signal, Var}
import ui.laminar.models.VisibilityFilter

object VisibilityFilterContext {
  val visibilityFilter: Var[VisibilityFilter]           = Var(VisibilityFilter.All)
  val writer: Observer[VisibilityFilter]                = visibilityFilter.writer
  def isSame(filter: VisibilityFilter): Signal[Boolean] = visibilityFilter.signal.map(_ == filter)
}
