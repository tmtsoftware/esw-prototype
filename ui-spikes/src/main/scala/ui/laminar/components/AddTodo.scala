package ui.laminar.components

import com.raquo.laminar.api.L._
import ui.laminar.stores.TodoListStore

object AddTodo {
  def apply(): Div = {
    val text1 = Var("")

    println("******************* error!!!")

    div(
      form(
        onSubmit.preventDefault.mapTo(text1.now()) --> TodoListStore.add,
        input(
          inContext(thisNode => onInput.mapTo(thisNode.ref.value) --> text1.writer)
        ),
        button(
          tpe := "submit",
          "Add Todo"
        )
      )
    )
  }
}
