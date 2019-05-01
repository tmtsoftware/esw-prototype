package ui.laminar.models

case class TodoList(todos: Vector[Todo]) {
  def add(text: String): TodoList = TodoList {
    todos :+ Todo(todos.length, text, isComplete = false)
  }

  def toggle(id: Int) = TodoList {
    todos.map { todo =>
      if (todo.id == id) todo.copy(isComplete = !todo.isComplete) else todo
    }
  }

  def filter(visibilityFilter: VisibilityFilter): TodoList = TodoList {
    visibilityFilter match {
      case VisibilityFilter.All       => todos
      case VisibilityFilter.Completed => todos.filter(_.isComplete)
      case VisibilityFilter.Active    => todos.filter(!_.isComplete)
    }
  }
}
