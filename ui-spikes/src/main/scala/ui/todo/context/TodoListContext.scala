package ui.todo.context

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{Context, FC, ProviderProps, ^ => React}
import ui.todo.models.{Todo, VisibilityFilter}

import scala.scalajs.js

object TodoListContext {
  val Context: Context[TodoListContext] = React.createContext(new TodoListContext(Seq.empty, _ => ()))

  val Provider: FC[Unit] = define.fc[Unit] { props =>
    val js.Tuple2(todos, setTodos) = React.useState(Seq.empty[Todo])

    Context.Provider(
      ProviderProps(
        new TodoListContext(todos, setTodos(_)),
        props.children.get
      )
    )
  }

}

class TodoListContext(todos: Seq[Todo], setTodos: Seq[Todo] => Unit) {
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
