package ui.laminar.stores

import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.{Signal, StrictSignal, Var}
import ui.laminar.models.VisibilityFilter

object VisibilityFilterStore {

  val value: Var[VisibilityFilter] = Var(VisibilityFilter.All)

  val update: Observer[VisibilityFilter]     = value.writer
  val signal: StrictSignal[VisibilityFilter] = value.signal

  def isSameAs(filter: VisibilityFilter): Signal[Boolean] = signal.map(_ == filter)

}
