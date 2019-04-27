package ui.mirror

import com.raquo.airstream.eventstream.EventStream
import com.raquo.airstream.signal.Signal

class Mirror(cell: Cell, store: Store, renderBackend: RenderBackend) extends MyOwner {
  val cells: EventStream[Cell]         = store.selectedCells.stream.filter(_.row == cell.row)
  val positions: EventStream[Position] = store.faultPositions.stream.filter(_ == cell.position)
  val onOff: Signal[Boolean]           = EventStream.merge(cells, positions).fold(false)((acc, _) => !acc)

  onOff.changes.foreach {
    case true  => store.faultCounter.increment()
    case false => store.faultCounter.decrement()
  }

  val color: Signal[String] = onOff.map {
    case false => List("#E7CFA0", "#7CC1D2", "#A97FFF")(cell.sector % 3)
    case true  => "red"
  }

  renderBackend.draw(cell, color, store)
}
