package ui.todo.models

import ui.todo.lib.ContextType

case class TodoList(todos: Seq[Todo], setTodos: Seq[Todo] => Unit) {
  def add(text: String): Unit = setTodos(todos :+ Todo(todos.length, text, isComplete = false))

  def toggle(id: Int): Unit = setTodos {
    todos.map { todo =>
      if (todo.id == id) todo.copy(isComplete = !todo.isComplete) else todo
    }
  }

  def filter(visibilityFilter: VisibilityFilter): Seq[Todo] = visibilityFilter match {
    case VisibilityFilter.All       => todos
    case VisibilityFilter.Completed => todos.filter(_.isComplete)
    case VisibilityFilter.Active    => todos.filter(!_.isComplete)
  }
}

object TodoList {
  def from(contextType: ContextType[Seq[Todo]]) = TodoList(contextType.value, contextType.set)
}