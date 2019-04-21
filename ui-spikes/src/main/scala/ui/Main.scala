package ui

import org.scalajs.dom
import ui.mirror.Boot
import ui.paper.PaperBackend
import ui.svg.SvgBackend

object Main {

  def main(arguments: Array[String]): Unit = {
//    dom.window.onload = Boot.start(PaperBackend)
    dom.window.onload = Boot.start(SvgBackend)
  }

}
