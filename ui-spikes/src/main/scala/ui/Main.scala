package ui

import org.scalajs.dom
//import ui.todo.components.App
import ui.laminar.components.App
import com.raquo.laminar.api.L._

object Main {

  def main(arguments: Array[String]): Unit = {
//    dom.window.onload = Boot.start(PaperBackend)
//    dom.window.onload = Boot.start(SvgBackend)
//    dom.window.onload = _ => App.run()
    render(dom.document.getElementById("todo"), App())
  }

}
