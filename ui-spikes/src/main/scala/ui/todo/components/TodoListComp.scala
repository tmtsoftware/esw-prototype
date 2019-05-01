package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.Context
import ui.todo.lib.JsUnit
import ui.todo.models.{Todo, TodoList}

object TodoListComp {

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    val todoList = TodoList.create()
    val filter   = Context.VisibilityFilter.useGetter()

    ul.noprops(
      todoList.filter(filter).map {
        case Todo(id, text1, isComplete) =>
          TodoItem.Component
            .withKey(id)
            .props(
              new TodoItem.Props(() => todoList.toggle(id), isComplete, text1)
            )
      }: _*
    )
  }
}
