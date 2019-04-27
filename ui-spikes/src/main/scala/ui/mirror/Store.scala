package ui.mirror

import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.eventstream.EventStream

class Store {
  trait State[T] {
    private val eventBus       = new EventBus[T]
    def set(value: T): Unit    = eventBus.writer.onNext(value)
    def stream: EventStream[T] = eventBus.events
  }

  object selectedCells  extends State[Cell]
  object faultPositions extends State[Position]
}
