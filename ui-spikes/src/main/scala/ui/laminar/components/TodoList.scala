package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.context.{TodoListContext, VisibilityFilterContext}

object TodoList {
  val dd = VisibilityFilterContext.visibilityFilter.signal.combineWith(TodoListContext.allTodos.signal).map {
    case (f, todos) => TodoListContext.filter(todos, f)
  }

  def apply(): Div = div {
    ul(
      children <-- dd.split(_.id)(TodoItem.apply)
    )
  }
}
