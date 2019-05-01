package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{ButtonHTMLAttributes, CSSProperties, FC, HTMLAttributes}
import ui.todo.context.VisibilityFilterContext
import ui.todo.models.VisibilityFilter

import scala.language.implicitConversions
import scala.scalajs.js

object Link {

  class Props(val filter: VisibilityFilter) extends js.Object
  object Props {
    def apply(filter: VisibilityFilter): Props = new Props(filter)
  }

  val Component: FC[Props] = define.fc[Props] { props =>
    val filterContext = VisibilityFilterContext.use()

    println(props.filter -> filterContext.get)

    button.props(
      ButtonHTMLAttributes(
        HTMLAttributes(
          onClick = _ => filterContext.set(props.filter),
          `aria-disabled` = props.filter == filterContext.get,
          style = new CSSProperties {
            marginLeft = "4px"
          }
        )
      ),
      props.children.getOrElse(null)
    )
  }
}
