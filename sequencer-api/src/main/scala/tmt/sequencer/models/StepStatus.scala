package tmt.sequencer.models

import enumeratum.{Enum, EnumEntry}

import scala.collection.immutable

sealed trait StepStatus extends EnumEntry

object StepStatus extends Enum[StepStatus] {
  override def values: immutable.IndexedSeq[StepStatus] = findValues

  case object Pending extends StepStatus

  case object InFlight extends StepStatus

  case object Finished extends StepStatus
}
