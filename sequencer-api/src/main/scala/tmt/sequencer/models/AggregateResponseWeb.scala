package tmt.sequencer.models
import upickle.default.{macroRW, ReadWriter => RW}

case class AggregateResponseWeb(childResponses: Set[CommandResponseWeb])

object AggregateResponseWeb {
  implicit val readWriter: RW[AggregateResponseWeb] = macroRW[AggregateResponseWeb]
}
