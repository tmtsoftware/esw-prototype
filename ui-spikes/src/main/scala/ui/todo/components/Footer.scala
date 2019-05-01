package ui.todo.components

import org.scalajs.dom
import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.lib.JsUnit
import ui.todo.models.VisibilityFilter

object Footer {

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    dom.console.log("-----------> Footer")

    div.noprops(
      span.noprops("Show: "),
      Link.Component.props(Link.Props(VisibilityFilter.All), VisibilityFilter.All.toString),
      Link.Component.props(Link.Props(VisibilityFilter.Active), VisibilityFilter.Active.toString),
      Link.Component.props(Link.Props(VisibilityFilter.Completed), VisibilityFilter.Completed.toString),
    )

  }
}
