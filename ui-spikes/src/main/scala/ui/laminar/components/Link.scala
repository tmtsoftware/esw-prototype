package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.context.VisibilityFilterContext
import ui.laminar.models.VisibilityFilter

object Link {
  def apply(filter: VisibilityFilter, content: String): Button = {
    button(
      onClick.mapTo(filter) --> VisibilityFilterContext.writer,
      disabled <-- VisibilityFilterContext.isSame(filter),
      marginLeft := "4px",
      content
    )
  }
}
