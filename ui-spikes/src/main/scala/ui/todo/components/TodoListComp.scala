package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.context.VisibilityFilterContext
import ui.todo.models.TodoList

import scala.scalajs.js

object TodoListComp {

  val Component: FC[_] = define.fc[js.Any] { _ =>
    val todoList      = TodoList.create()
    val filterContext = VisibilityFilterContext.use()

    ul.noprops(
      todoList.filter(filterContext.value).map { todo =>
        TodoItem.Component
          .withKey(todo.id)
          .props(
            new TodoItem.Props(() => todoList.toggle(todo.id), todo.isComplete, todo.text)
          )
      }: _*
    )
  }
}
