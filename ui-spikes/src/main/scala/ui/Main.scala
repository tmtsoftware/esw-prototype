package ui

import org.scalajs.dom
import ui.animations.Rays
import ui.config.components.MuiApp
import ui.todo.components.ReactApp
import ui.laminar.components.LaminarApp
import ui.mirror.Boot
import ui.mirror.svg.SvgBackend

import scala.scalajs.js

object Main {

  def main(arguments: Array[String]): Unit = {

//    dom.window.onload = Boot.start(PaperBackend)
//    dom.window.onload = Boot.start(SvgBackend)
//    ReactApp.render()
//    LaminarApp.renderApp()
     MuiApp.render()
//    LaminarApp.renderApp()
//    new Rays().animate()
  }

}
