package ui.laminar.stores

import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.{Signal, StrictSignal, Var}
import ui.laminar.models.{TodoList, VisibilityFilter}

object TodoListStore {
  val allTodos: Var[TodoList]        = Var(TodoList(Vector.empty))
  val signal: StrictSignal[TodoList] = allTodos.signal

  val add: Observer[String] = Observer { text =>
    allTodos.update(_.add(text))
    VisibilityFilterStore.visibilityFilter.update(_ => VisibilityFilter.All)
  }

  val toggle: Observer[Int] = Observer { id =>
    allTodos.update(_.toggle(id))
  }

  val filteredList: Signal[TodoList] = signal.combineWith(VisibilityFilterStore.signal).map {
    case (todoList, filter) => todoList.filter(filter)
  }

}
