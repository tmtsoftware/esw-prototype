package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore

object TodoList {

  def apply(): Div = div {
    ul(
      children <-- TodoListStore.filteredList.map(_.todos).split(_.id)(TodoItem.apply)
    )
  }
}
