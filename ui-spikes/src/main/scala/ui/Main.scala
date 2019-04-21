package ui

import org.scalajs.dom
import ui.paper.Boot

object Main {

  def main(arguments: Array[String]): Unit = {
    dom.window.onload = Boot.start
  }

}
