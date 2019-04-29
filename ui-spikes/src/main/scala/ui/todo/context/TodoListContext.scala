package ui.todo.context

import ui.todo.models.{Todo, VisibilityFilter}

object TodoListContext extends GenericContext(Seq.empty[Todo], new TodoListContext(_, _))

case class TodoListContext(todos: Seq[Todo], setTodos: Seq[Todo] => Unit) {
  def add(text: String): Unit = setTodos(todos :+ Todo(todos.length, text, isComplete = false))

  def toggle(id: Int): Unit = setTodos {
    todos.map { todo =>
      if (todo.id == id) todo.copy(isComplete = !todo.isComplete) else todo
    }
  }

  def filter(visibilityFilter: VisibilityFilter): Seq[Todo] = visibilityFilter.Value match {
    case VisibilityFilter.All.Value       => todos
    case VisibilityFilter.Completed.Value => todos.filter(_.isComplete)
    case VisibilityFilter.Active.Value    => todos.filter(!_.isComplete)
  }
}
