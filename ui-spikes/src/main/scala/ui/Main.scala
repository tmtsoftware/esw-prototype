package ui

//import org.scalajs.dom
import ui.animations.Rays
import ui.todo.components.ReactApp
import ui.laminar.components.LaminarApp

object Main {

  def main(arguments: Array[String]): Unit = {
//    dom.window.onload = Boot.start(PaperBackend)
//    dom.window.onload = Boot.start(SvgBackend)
//    ReactApp.render()
//    LaminarApp.renderApp()
    new Rays().animate()
  }

}
