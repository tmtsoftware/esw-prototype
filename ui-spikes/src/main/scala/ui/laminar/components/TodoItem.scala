package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.context.TodoListContext
import ui.laminar.models.Todo

object TodoItem {
  def apply(id: Int, initialTodo: Todo, todos: Signal[Todo]): Li = {
    li(
      onClick.mapTo(id) --> TodoListContext.toggle,
      textDecoration <-- todos.map(x => if (x.isComplete) "line-through" else "none"),
      child.text <-- todos.map(_.text)
    )
  }
}
