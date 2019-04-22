package ui.mirror

import com.raquo.airstream.eventstream.EventStream
import com.raquo.airstream.signal.Signal

class Mirror(cell: Cell, store: Store, renderBackend: RenderBackend) extends MyOwner {
  val cells: EventStream[Cell]         = store.selectedCells.filter(_.row == cell.row)
  val positions: EventStream[Position] = store.faultPositions.filter(_ == cell.position)
  val signal: Signal[Boolean]          = EventStream.merge(cells, positions).fold(false)((acc, _) => !acc)

  val color: Signal[String] = signal.map {
    case false => List("#E7CFA0", "#7CC1D2", "#A97FFF")(cell.sector % 3)
    case true  => "red"
  }

  renderBackend.draw(cell, color, store)
}
