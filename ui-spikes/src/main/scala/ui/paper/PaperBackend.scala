package ui.paper

import com.raquo.airstream.signal.Signal
import org.scalajs.dom.document
import typings.stdLib.HTMLCanvasElement
import typings.paperLib.paperMod.{^ => Paper}
import ui.mirror.{Cell, Point, RenderBackend, Store}

object PaperBackend extends RenderBackend {
  override def center: Point = {
    val paperCenter = Paper.view.center
    Point(paperCenter.x, paperCenter.y)
  }

  override def setup(): Unit = {
    val canvas = document.getElementById("myCanvas").asInstanceOf[HTMLCanvasElement]
    canvas.width = 900
    canvas.height = 800
    Paper.setup(canvas)
  }

  override def draw(cell: Cell, color: Signal[String], store: Store): Unit = {
    new PaperCellComp(cell, color, store)
  }

  override def postDraw(): Unit = {
    Paper.view.draw()
  }
}
