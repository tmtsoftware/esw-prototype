package ui.todo.components

import org.scalajs.dom
import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.models.VisibilityFilter

object Footer {

  val Component: FC[Unit] = define.fc[Unit] { _ =>
    dom.console.log("-----------> Footer")

    div.noprops(
      span.noprops("Show: "),
      Link.Component.props(VisibilityFilter.All, VisibilityFilter.All.toString),
      Link.Component.props(VisibilityFilter.Active, VisibilityFilter.Active.toString),
      Link.Component.props(VisibilityFilter.Completed, VisibilityFilter.Completed.toString),
    )

  }
}
