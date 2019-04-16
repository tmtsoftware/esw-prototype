package ui

import typings.stdLib.^.window
import ui.paper.Boot

object Main {

  def main(arguments: Array[String]): Unit = {
    window.onload = Boot.start
  }

}
