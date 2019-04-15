package ui.paper

import typings.stdLib.^.document
import typings.stdLib.{Event, HTMLCanvasElement, Window}
import typings.paperLib.paperMod.{^ => Paper}

import scala.scalajs.js

object Boot {

  val start: js.ThisFunction1[Window, Event, _] = { (_, _) =>
    val canvas: HTMLCanvasElement = document.getElementById("myCanvas").asInstanceOf[HTMLCanvasElement]
    Paper.setup(canvas)
    new Hexagon(50)
    Paper.view.draw()
  }

}
