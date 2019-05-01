package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.VisibilityFilterStore
import ui.laminar.models.VisibilityFilter

object Link {
  def apply(filter: VisibilityFilter, content: String): Button = {
    button(
      onClick.mapTo(filter) --> VisibilityFilterStore.update,
      disabled <-- VisibilityFilterStore.isSame(filter),
      marginLeft := "4px",
      content
    )
  }
}
