package ui.mirror

import com.raquo.airstream.eventbus.{EventBus, WriteBus}
import com.raquo.airstream.eventstream.EventStream

class Store {
  val eventBus = new EventBus[AppEvent]

  val writer: WriteBus[AppEvent]    = eventBus.writer
  val events: EventStream[AppEvent] = eventBus.events

  val selectedCells: EventStream[Cell] = events.collect {
    case SelectEvent(cell) => cell
  }

  val faultPositions: EventStream[Position] = events.collect {
    case FaultEvent(position) => position
  }
}

sealed trait AppEvent
case class SelectEvent(cell: Cell)        extends AppEvent
case class FaultEvent(position: Position) extends AppEvent
