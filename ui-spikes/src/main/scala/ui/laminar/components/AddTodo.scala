package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore
import ui.laminar.stores.TodoListAction._

object AddTodo {

  def apply(): Div = {

    println("**** rendering AddTodo")

    val inputValue = Var("")

    div(
      form(
        onSubmit.preventDefault.mapTo(Add(inputValue.now())) --> TodoListStore.Reducer,
        input(
          inContext(thisNode => onInput.mapTo(thisNode.ref.value) --> inputValue.writer)
        ),
        button(
          tpe := "submit",
          "Add Todo"
        )
      )
    )
  }

}
