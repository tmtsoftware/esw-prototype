package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{ButtonHTMLAttributes, CSSProperties, FC, HTMLAttributes}
import ui.todo.context.VisibilityFilterContext
import ui.todo.models.VisibilityFilter

object Link {

  val Component: FC[VisibilityFilter] = define.fc[VisibilityFilter] { filter =>
    val filterContext = VisibilityFilterContext.use()

    println(filter.Value -> filterContext.value.Value)

    button.props(
      ButtonHTMLAttributes(
        HTMLAttributes(
          onClick = e => filterContext.set(filter),
          `aria-disabled` = filter == filterContext.value,
          style = new CSSProperties {
            marginLeft = "4px"
          }
        )
      ),
      filter.children.getOrElse(null)
    )
  }
}
