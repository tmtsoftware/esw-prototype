package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.models.VisibilityFilter

object Footer {
  def apply(): Div = {
    div(
      span("Show: "),
      Link(VisibilityFilter.All, VisibilityFilter.All.toString),
      Link(VisibilityFilter.Active, VisibilityFilter.Active.toString),
      Link(VisibilityFilter.Completed, VisibilityFilter.Completed.toString),
    )
  }
}
