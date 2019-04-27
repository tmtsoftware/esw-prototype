package ui.mirror.svg

import com.raquo.airstream.signal.Signal
import typings.svgDotJsLib.svgDotJsMod.svgjsNs.Doc
import typings.svgDotJsLib.svgDotJsMod.{^ => SVG}
import ui.mirror._

object SvgBackend extends RenderBackend {

  var doc: Doc = _

  override def center: Point = {
    new Point(doc.width() / 2, doc.height() / 2)
  }

  override def setup(width: Int, height: Int, store: Store): Unit = {
    doc = SVG("mySvg").size(width, height)
    new SvgCounterComp(doc, store)
  }

  override def draw(cell: Cell, color: Signal[String], store: Store): Unit = {
    new SvgCellComp(cell, color, store)
  }

  override def postDraw(): Unit = {}
}
