package tmt.sequencer.models

import upickle.default.{macroRW, ReadWriter => RW}
import enumeratum.{Enum, EnumEntry}
import tmt.sequencer.utils.EnumUpickleSupport
import ujson.Js

import scala.collection.immutable

case class AggregateResponseWeb(childResponses: Set[CommandResponseWeb])

object AggregateResponseWeb {
  implicit val readWriter: RW[AggregateResponseWeb] = macroRW[AggregateResponseWeb]
}

case class CommandListWeb(commands: Seq[SequenceCommandWeb])

object CommandListWeb {
  implicit val readWriter: RW[CommandListWeb] = macroRW[CommandListWeb]
}

case class CommandResponseWeb(runId: String, resultType: String, payload: String)

object CommandResponseWeb {
  implicit val readWriter: RW[CommandResponseWeb] = macroRW[CommandResponseWeb]
}

case class SequenceCommandWeb(
    kind: String,
    runId: String,
    source: String,
    commandName: String,
    maybeObsId: Option[String],
    paramSet: Js.Arr
)

object SequenceCommandWeb {
  implicit val readWriter: RW[SequenceCommandWeb] = macroRW[SequenceCommandWeb]
}

case class SequenceWeb(steps: List[StepWeb])
object SequenceWeb {
  implicit val readWriter: RW[SequenceWeb] = macroRW[SequenceWeb]
}

sealed trait StepStatus extends EnumEntry

object StepStatus extends Enum[StepStatus] {
  override def values: immutable.IndexedSeq[StepStatus] = findValues
  case object Pending  extends StepStatus
  case object InFlight extends StepStatus
  case object Finished extends StepStatus

  implicit val rw: RW[StepStatus] = EnumUpickleSupport.enumFormat
}

case class StepWeb(command: SequenceCommandWeb, status: StepStatus, hasBreakpoint: Boolean)

object StepWeb {
  implicit val readWriter: RW[StepWeb] = macroRW[StepWeb]
}
