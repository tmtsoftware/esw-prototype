package ui.svg

import com.raquo.airstream.signal.Signal
import typings.svgDotJsLib.svgDotJsMod.svgjsNs.Doc
import typings.svgDotJsLib.svgDotJsMod.{^ => SVG}
import ui.mirror._

object SvgBackend extends RenderBackend with MyOwner {

  var doc: Doc = _

  override def center: Point = {
    val point = new Point(doc.width() / 2, doc.height() / 2)
    println(point)
    point
  }

  override def setup(): Unit = {
    doc = SVG("mySvg").size(900, 800)
  }

  override def draw(cell: Cell, color: Signal[String], store: Store): Unit = {
    new SvgCellComp(cell, color, store)
  }

  override def postDraw(): Unit = {}
}
