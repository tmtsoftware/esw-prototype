package tmt.sequencer.models
import upickle.default.{macroRW, ReadWriter => RW}

case class StepWeb(command: SequenceCommandWeb, status: StepStatus, hasBreakpoint: Boolean)

object StepWeb {
  import EnumUpickleSupport._
  implicit val readWriter: RW[StepWeb] = macroRW[StepWeb]
}
