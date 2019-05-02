package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore
import ui.laminar.models.Todo

object TodoItem {

  def apply(id: Int, todoItem: Todo, todos: Signal[Todo]): Li = {

    println(s"**** rendering TodoItem=$todoItem")

    li(
      onClick.mapTo(id) --> TodoListStore.toggle,
      textDecoration <-- todos.map(x => if (x.isComplete) "line-through" else "none"),
      child.text <-- todos.map(_.text)
    )
  }

}
