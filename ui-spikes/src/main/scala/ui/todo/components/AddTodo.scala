package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{ButtonHTMLAttributes, FC, FormHTMLAttributes, HTMLAttributes, InputHTMLAttributes, ^ => React}
import ui.todo.context.TodoListContext
import ui.todo.lib.GenericState
import ui.todo.models.TodoList

import scala.scalajs.js

object AddTodo {

  val Component: FC[_] = define.fc[js.Any] { _ =>
    val todoList               = TodoList.from(TodoListContext.use())
    val (inputValue, setInput) = GenericState.use("")

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
