package ui.todo.models

import ui.todo.context.Context

case class TodoList(todos: Seq[Todo], setTodos: Seq[Todo] => Unit, setFilter: VisibilityFilter => Unit) {
  def add(text: String): Unit = {
    setTodos(todos :+ Todo(todos.length, text, isComplete = false))
    setFilter(VisibilityFilter.All)
  }

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
  def create(): TodoList = {
    val (todos, setTodos) = Context.TodoList.use()
    val setFilter         = Context.VisibilityFilter.useSetter()
    TodoList(todos, setTodos, setFilter)
  }
}
