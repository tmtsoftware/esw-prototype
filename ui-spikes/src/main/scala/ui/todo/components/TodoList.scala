package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{FC, ^ => React}
import ui.todo.context.{TodoListContext, VisibilityFilterContext}

object TodoList {

  val Component: FC[Unit] = define.fc[Unit] { _ =>
    val todoListContext         = React.useContext(TodoListContext.Context)
    val visibilityFilterContext = React.useContext(VisibilityFilterContext.Context)

    ul.noprops(
      todoListContext.filter(visibilityFilterContext.filter).map { todo =>
        TodoItem.Component
          .withKey(todo.id)
          .props(
            TodoItem.Props(() => todoListContext.toggle(todo.id), todo.isComplete, todo.text)
          )
      }: _*
    )
  }
}
