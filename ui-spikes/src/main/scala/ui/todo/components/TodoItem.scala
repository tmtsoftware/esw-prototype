package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{CSSProperties, FC, LiHTMLAttributes}

import scala.scalajs.js

object TodoItem {

  class Props(val onClick: () => Unit, val isComplete: Boolean, val textContent: String) extends js.Object

  val Component: FC[Props] = define.fc[Props] { props =>
    println(s"**** rendering TodoItem isComplete=${props.isComplete}, text=${props.textContent}")

    li.props(
      LiHTMLAttributes(
        onClick = _ => props.onClick(),
        style = new CSSProperties {
          textDecoration = if (props.isComplete) "line-through" else "none"
        }
      ),
      props.textContent
    )
  }
}
