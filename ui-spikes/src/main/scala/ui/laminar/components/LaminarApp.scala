package ui.laminar.components

import com.raquo.laminar.api.L._
import org.scalajs.dom

object LaminarApp {

  def renderApp(): Root = render(
    dom.document.getElementById("todo"),
    LaminarApp()
  )

  def apply(): Div = {

    println("**** rendering App")

    div(
      AddTodo(),
      TodoListComp(),
      Footer()
    )
  }

}
