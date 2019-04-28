package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.{TodoListContext, VisibilityFilterContext}

import scala.scalajs.js

object App {
  val Component: FC[_] = define.fc[js.Any] { _ =>
    div.noprops(
      VisibilityFilterContext.Provider.noprops(
        TodoListContext.Provider.noprops(
          AddTodo.Component.noprops(),
          TodoList.Component.noprops()
        ),
        Footer.Component.noprops()
      )
    )
  }
}
