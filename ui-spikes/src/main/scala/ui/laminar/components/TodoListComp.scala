package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore

object TodoListComp {

  def apply(): Div = {

    println("**** rendering TodoList")

    div {
      ul(
        children <-- TodoListStore.filteredList.map(_.todos).split(_.id)(TodoItem.apply)
      )
    }
  }
}
