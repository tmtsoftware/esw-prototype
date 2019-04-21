package ui.paper

import com.raquo.airstream.eventbus.{EventBus, WriteBus}
import com.raquo.airstream.eventstream.EventStream

class Store {
  val eventBus = new EventBus[AppEvent]

  val writer: WriteBus[AppEvent]    = eventBus.writer
  val events: EventStream[AppEvent] = eventBus.events

  val cellClicks: EventStream[Cell] = events.collect {
    case Click(cell) => cell
  }

  val positions: EventStream[Position] = events.collect {
    case PositionEvent(position) => position
  }
}

sealed trait AppEvent
case class Click(cell: Cell)                 extends AppEvent
case class PositionEvent(position: Position) extends AppEvent
