package ui

import org.scalajs.dom
import ui.todo.components.App

object Main {

  def main(arguments: Array[String]): Unit = {
//    dom.window.onload = Boot.start(PaperBackend)
//    dom.window.onload = Boot.start(SvgBackend)
    dom.window.onload = _ => App.run()
  }

}
