package ui.paper

import org.scalajs.dom.document
import org.scalajs.dom.Event
import typings.paperLib.paperMod.{^ => Paper}
import typings.stdLib.HTMLCanvasElement

object Boot {

  val start: Event => Unit = { _ =>
    val store = new Store
    new ExternalService(store)

    Paper.setup(
      document.getElementById("myCanvas").asInstanceOf[HTMLCanvasElement]
    )
    val paperCenter = Paper.view.center
    val center      = Point(paperCenter.x, paperCenter.y)
    new Display(store).render(center, 16, 13)
    Paper.view.draw()
  }

}
