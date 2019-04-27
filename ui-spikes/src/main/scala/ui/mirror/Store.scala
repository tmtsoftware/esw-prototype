package ui.mirror

import ui.events.{Counter, State}

class Store {
  object selectedCells  extends State[Cell]
  object faultPositions extends State[Position]
  object faultCounter   extends Counter
}
