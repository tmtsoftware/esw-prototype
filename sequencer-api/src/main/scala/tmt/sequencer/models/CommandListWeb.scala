package tmt.sequencer.models
import upickle.default.{macroRW, ReadWriter => RW}

case class CommandListWeb(commands: Seq[SequenceCommandWeb])

object CommandListWeb {
  implicit val readWriter: RW[CommandListWeb] = macroRW[CommandListWeb]
}
