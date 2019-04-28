package ui.todo.components

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{ButtonHTMLAttributes, CSSProperties, FC, HTMLAttributes, ^ => React}
import ui.todo.context.VisibilityFilterContext
import ui.todo.models.VisibilityFilter

object Link {

  val Component: FC[VisibilityFilter] = define.fc[VisibilityFilter] { filter =>
    val context = React.useContext(VisibilityFilterContext.Context)

    button.props(
      ButtonHTMLAttributes(
        HTMLAttributes(
          onClick = e => context.set(filter),
          `aria-disabled` = filter == context.filter,
          style = new CSSProperties {
            marginLeft = "4px"
          }
        )
      ),
      filter.children.get
    )

  }
}
