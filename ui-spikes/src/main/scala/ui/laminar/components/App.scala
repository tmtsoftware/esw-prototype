package ui.laminar.components

import com.raquo.laminar.api.L._

object App {

  def apply(): Div = {

    println("**** rendering App")

    div(
      AddTodo(),
      TodoListComp(),
      Footer()
    )
  }

}
