package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{CSSProperties, FC, LiHTMLAttributes}

object TodoItem {

  case class Props(onClick: () => Unit, isComplete: Boolean, textContent: String)

  val Component: FC[Props] = define.fc[Props] { props =>
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
