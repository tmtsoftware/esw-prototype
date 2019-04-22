package ui.paper

import com.raquo.airstream.signal.Signal
import typings.paperLib.paperMod.{Path, Point => PPoint}
import typings.paperLib.paperNs
import ui.mirror._

import scala.scalajs.js
import scala.scalajs.js.|

class PaperCellComp(cell: Cell, color: Signal[String], store: Store) extends MyOwner {
  val points: js.Array[PPoint] = {
    val vertices: List[Point] = cell.hexagon.vertices
    vertices.map(p => new PPoint(p.x, p.y)).to[js.Array]
  }

  new Path(points) {
    color.foreach(x => fillColor = x)
    strokeColor = "white"
    override def onClick(event: paperNs.MouseEvent): Unit | Boolean = store.writer.onNext(SelectEvent(cell))
  }
}
