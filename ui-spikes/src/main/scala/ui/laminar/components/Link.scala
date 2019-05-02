package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.VisibilityFilterStore
import ui.laminar.models.VisibilityFilter

object Link {
  def apply(filter: VisibilityFilter, childNodes: Node*): Button = {

    println(s"**** rendering Link filter=$filter")

    button(
      onClick.mapTo(filter) --> VisibilityFilterStore.update,
      disabled <-- VisibilityFilterStore.isSameAs(filter),
      marginLeft := "4px",
      childNodes
    )
  }
}
