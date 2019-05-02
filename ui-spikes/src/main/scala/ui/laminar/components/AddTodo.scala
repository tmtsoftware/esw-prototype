package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore

object AddTodo {

  def apply(): Div = {

    println("**** rendering AddTodo")

    val inputValue = Var("")

    div(
      form(
        onSubmit.preventDefault.mapTo(inputValue.now()) --> TodoListStore.add,
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
