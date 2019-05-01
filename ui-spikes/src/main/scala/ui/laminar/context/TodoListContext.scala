package ui.laminar.context

import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.Var
import ui.laminar.models.{Todo, VisibilityFilter}

object TodoListContext {
  val allTodos: Var[Vector[Todo]] = Var(Vector.empty[Todo])

  val add: Observer[String] = Observer { text =>
    allTodos.update(todos => todos :+ Todo(todos.length, text, isComplete = false))
    VisibilityFilterContext.visibilityFilter.update(x => VisibilityFilter.All)
  }

  val toggle: Observer[Int] = Observer { id =>
    allTodos.update { todos =>
      todos.map { todo =>
        if (todo.id == id) todo.copy(isComplete = !todo.isComplete) else todo
      }
    }
  }

  def filter(todos: Vector[Todo], visibilityFilter: VisibilityFilter): Vector[Todo] = visibilityFilter match {
    case VisibilityFilter.All       => todos
    case VisibilityFilter.Completed => todos.filter(_.isComplete)
    case VisibilityFilter.Active    => todos.filter(!_.isComplete)
  }
}
