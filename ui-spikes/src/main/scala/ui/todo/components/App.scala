package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.{TodoListContext, VisibilityFilterContext}
import ui.todo.lib.{ComponentUtils, JsUnit}

object App {

  val Provider: FC[JsUnit] = ComponentUtils.compose(
    VisibilityFilterContext.Provider,
    TodoListContext.Provider
  )

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    Provider.noprops(
      AddTodo.Component.noprops(),
      TodoListComp.Component.noprops(),
      Footer.Component.noprops()
    )
  }

}
