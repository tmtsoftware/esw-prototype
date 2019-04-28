package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{ButtonHTMLAttributes, FC, FormHTMLAttributes, HTMLAttributes, InputHTMLAttributes, ^ => React}
import ui.todo.context.TodoListContext

import scala.scalajs.js

object AddTodo {

  val Component: FC[_] = define.fc[js.Any] { _ =>
    val context                         = React.useContext(TodoListContext.Context)
    val js.Tuple2(inputValue, setInput) = React.useState("")

    form.props(
      FormHTMLAttributes(
        HTMLAttributes(
          onSubmit = { e =>
            e.preventDefault()
            context.add(inputValue)
          }
        ),
      ),
      input.props(
        InputHTMLAttributes(
          onChange = e => setInput(e.currentTarget.value)
        )
      ),
      button.props(
        ButtonHTMLAttributes(
          HTMLAttributes(typeof = "submit")
        ),
        "Add todo"
      )
    )
  }
}
