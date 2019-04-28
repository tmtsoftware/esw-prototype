package ui.todo

import typings.reactDashDomLib.reactDashDomMod.{^ => ReactDom}
import typings.reactLib.dsl._
import typings.stdLib.{^, Element}
import ui.todo.components.App

object TodoEntryPoint {

  def run(): Unit = {
    ReactDom.render(
      App.Component.noprops(),
      ^.document.getElementById("todo").asInstanceOf[Element]
    )
  }

}
