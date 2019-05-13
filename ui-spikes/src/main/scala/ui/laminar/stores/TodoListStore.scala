package ui.laminar.stores

import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.{Signal, StrictSignal, Var}
import ui.laminar.models.{TodoList, VisibilityFilter}

sealed trait TodoListAction
object TodoListAction {
  case class Add(text: String) extends TodoListAction
  case class Toggle(id: Int)   extends TodoListAction
}

object TodoListStore {
  import TodoListAction._

  private val allTodos: Var[TodoList] = Var(TodoList(Vector.empty))
  val signal: StrictSignal[TodoList]  = allTodos.signal

  val Reducer: Observer[TodoListAction] = Observer {
    case Add(text) =>
      allTodos.update(_.add(text))
      VisibilityFilterStore.value.update(_ => VisibilityFilter.All)
    case Toggle(id) =>
      allTodos.update(_.toggle(id))
  }

  val filteredList: Signal[TodoList] = signal.combineWith(VisibilityFilterStore.signal).map {
    case (todoList, filter) => todoList.filter(filter)
  }

}
