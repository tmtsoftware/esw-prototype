package tmt.sequencer.models

import ujson.Js
import upickle.default.{macroRW, ReadWriter => RW}

case class SequenceCommandWeb(
    kind: String,
    runId: String,
    source: String,
    commandName: String,
    maybeObsId: Option[String],
    paramSet: String
)

object SequenceCommandWeb {
  implicit val readWriter: RW[SequenceCommandWeb] = macroRW[SequenceCommandWeb]
}
