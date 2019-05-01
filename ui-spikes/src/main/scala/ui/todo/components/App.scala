package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.{TodoListContext, VisibilityFilterContext}
import ui.todo.lib.ComponentUtils

import scala.scalajs.js

object App {

  val Provider: FC[_] = ComponentUtils.compose(
    VisibilityFilterContext.Provider,
    TodoListContext.Provider
  )

  val Component: FC[_] = define.fc[js.Any] { _ =>
    Provider.noprops(
      AddTodo.Component.noprops(),
      TodoListComp.Component.noprops(),
      Footer.Component.noprops()
    )
  }

}
