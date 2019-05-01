package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod._
import ui.todo.lib.{GenericState, JsUnit}
import ui.todo.models.TodoList

object AddTodo {

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    val todoList               = TodoList.create()
    val (inputValue, setInput) = GenericState.use("")

    println("******************* error!!!")

    form.props(
      FormHTMLAttributes(
        HTMLAttributes(
          onSubmit = { e =>
            e.preventDefault()
            todoList.add(inputValue)
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
