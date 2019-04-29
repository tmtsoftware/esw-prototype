package ui.todo.components

import org.scalajs.dom
import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import ui.todo.models.VisibilityFilter

import scala.scalajs.js

object Footer {

  val Component: FC[_] = define.fc[js.Any] { _ =>
    dom.console.log("-----------> Footer")

    div.noprops(
      span.noprops("Show: "),
      Link.Component.props(VisibilityFilter.All, VisibilityFilter.All.Value),
      Link.Component.props(VisibilityFilter.Active, VisibilityFilter.Active.Value),
      Link.Component.props(VisibilityFilter.Completed, VisibilityFilter.Completed.Value),
    )

  }
}
