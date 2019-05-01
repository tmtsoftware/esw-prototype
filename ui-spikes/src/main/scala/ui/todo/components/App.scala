package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.Context
import ui.todo.lib.JsUnit

object App {

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    Context.Provider.noprops(
      AddTodo.Component.noprops(),
      TodoListComp.Component.noprops(),
      Footer.Component.noprops()
    )
  }

}
