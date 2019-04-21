package ui.paper

import com.raquo.airstream.signal.{Signal, Var}

class Mirror(cell: Cell, store: Store) extends MyOwner {
  store.cellClicks.filter(_.row == cell.row).foreach(_ => click())
  store.positions.filter(_ == cell.position).foreach(_ => click())

  def click(): Unit = clicked.set(!clicked.now())

  private val clicked = Var(false)

  val color: Signal[String] = clicked.signal.map {
    case false => List("#E7CFA0", "#7CC1D2", "#A97FFF")(cell.sector % 3)
    case true  => "red"
  }

  new PaperPathComp(cell, color, store)
}
