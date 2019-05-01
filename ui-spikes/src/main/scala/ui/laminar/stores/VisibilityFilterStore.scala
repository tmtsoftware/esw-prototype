package ui.laminar.stores

import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.{Signal, StrictSignal, Var}
import ui.laminar.models.VisibilityFilter

object VisibilityFilterStore {
  val visibilityFilter: Var[VisibilityFilter]           = Var(VisibilityFilter.All)
  val update: Observer[VisibilityFilter]                = visibilityFilter.writer
  val signal: StrictSignal[VisibilityFilter]            = visibilityFilter.signal
  def isSame(filter: VisibilityFilter): Signal[Boolean] = signal.map(_ == filter)
}
