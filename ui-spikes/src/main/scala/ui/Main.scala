package ui

import org.scalajs.dom
import ui.mirror.Boot
import ui.mirror.paper.PaperBackend
import ui.mirror.svg.SvgBackend

object Main {

  def main(arguments: Array[String]): Unit = {
//    dom.window.onload = Boot.start(PaperBackend)
    dom.window.onload = Boot.start(SvgBackend)
  }

}
