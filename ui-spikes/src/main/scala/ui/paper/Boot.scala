package ui.paper

import org.scalajs.dom
import org.scalajs.dom.Event
import typings.paperLib.paperMod.{^ => Paper}
import typings.stdLib.HTMLCanvasElement

object Boot {

  val start: Event => Unit = { _ =>
    val canvas: HTMLCanvasElement = dom.document
      .getElementById("myCanvas")
      .asInstanceOf[HTMLCanvasElement]

    canvas.width = 900
    canvas.height = 800

    Paper.setup(canvas)
    new Display(16, 13).render()
    Paper.view.draw()
  }

}
