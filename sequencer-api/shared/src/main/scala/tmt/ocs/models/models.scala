package tmt.ocs.models

import enumeratum.{Enum, EnumEntry}

import scala.collection.immutable

sealed trait StepStatus extends EnumEntry

object StepStatus extends Enum[StepStatus] {
  override def values: immutable.IndexedSeq[StepStatus] = findValues
  case object Pending  extends StepStatus
  case object InFlight extends StepStatus
  case object Finished extends StepStatus
}

case class SequencerInfo(id: String, mode: String)

sealed trait RequestComponent
object RequestComponent {
  case class FilterWheel(name: String) extends RequestComponent
  case class Disperser(name: String)   extends RequestComponent
}

case class PositionResponse(requestComponent: RequestComponent, position: Int)
