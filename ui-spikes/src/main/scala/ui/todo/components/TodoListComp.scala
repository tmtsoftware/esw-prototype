package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.VisibilityFilterContext
import ui.todo.models.TodoList

import scala.scalajs.js

object TodoListComp {

  val Component: FC[_] = define.fc[js.Any] { _ =>
    val todoList = TodoList.create()
    val filter   = VisibilityFilterContext.useGetter()

    ul.noprops(
      todoList.filter(filter).map { todo =>
        TodoItem.Component
          .withKey(todo.id)
          .props(
            new TodoItem.Props(() => todoList.toggle(todo.id), todo.isComplete, todo.text)
          )
      }: _*
    )
  }
}
