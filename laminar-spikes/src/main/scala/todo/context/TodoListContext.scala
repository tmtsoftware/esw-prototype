package todo.context

import com.raquo.airstream.signal.{Signal, Var}
import todo.models.{Todo, VisibilityFilter}

object TodoListContext {
  val allTodos: Var[Vector[Todo]] = Var(Vector.empty[Todo])

  def add(text: String): Unit = {
    allTodos.update(todos => todos :+ Todo(todos.length, text, isComplete = false))
  }

  def toggle(id: Int): Unit = allTodos.update { todos =>
    todos.map { todo =>
      if (todo.id == id) todo.copy(isComplete = !todo.isComplete) else todo
    }
  }

  def filter(visibilityFilter: VisibilityFilter): Signal[Vector[Todo]] = allTodos.signal.map { todos =>
    visibilityFilter match {
      case VisibilityFilter.All       => todos
      case VisibilityFilter.Completed => todos.filter(_.isComplete)
      case VisibilityFilter.Active    => todos.filter(!_.isComplete)
    }
  }
}
