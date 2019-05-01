package todo

import com.raquo.laminar.api.L._
import org.scalajs.dom
import ui.laminar.components.App

object Main {
  def main(args: Array[String]): Unit = {
    render(dom.document.getElementById("todo"), App())
  }
}
