package ui.mirror.svg

import com.raquo.airstream.signal.Signal
import org.scalajs.dom.raw.MouseEvent
import typings.svgDotJsLib.svgDotJsMod.Polygon
import ui.mirror.svg.SvgBackend.doc
import ui.mirror.{Cell, MyOwner, Store}

import scala.scalajs.js

class SvgCellComp(cell: Cell, color: Signal[String], store: Store) extends MyOwner {
  val points: js.Array[js.Array[Double]] = {
    val vertices = cell.hexagon.vertices
    vertices.map(v => js.Array(v.x, v.y)).to[js.Array]
  }

  val polygon: Polygon = doc
    .polygon(points)
    .stroke("white")
    .on("click", { _: MouseEvent =>
      store.selectedCells.set(cell)
    })

  color.foreach(c => polygon.fill(c))
}
