package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.{TodoListContext, VisibilityFilterContext}

object App {
  val Component: FC[Unit] = define.fc[Unit] { _ =>
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
