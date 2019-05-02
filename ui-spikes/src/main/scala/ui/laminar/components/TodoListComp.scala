package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore

object TodoListComp {

  def apply(): Node = {

    println("**** rendering TodoList")

    ul(
      children <-- TodoListStore.filteredList.map(_.todos).split(_.id)(TodoItem.apply)
    )
  }
}
