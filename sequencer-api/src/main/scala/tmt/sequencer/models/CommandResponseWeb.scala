package tmt.sequencer.models
import upickle.default.{macroRW, ReadWriter => RW}

case class CommandResponseWeb(runId: String, resultType: String, payload: String)

object CommandResponseWeb {
  implicit val readWriter: RW[CommandResponseWeb] = macroRW[CommandResponseWeb]
}
