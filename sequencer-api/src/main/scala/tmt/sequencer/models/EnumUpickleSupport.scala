package tmt.sequencer.models

import enumeratum.{Enum, EnumEntry}
import upickle.default.{ReadWriter => RW}

object EnumUpickleSupport {
  implicit def enumFormat[T <: EnumEntry: Enum]: RW[T] =
    upickle.default
      .readwriter[String]
      .bimap[T](
        _.entryName,
        implicitly[Enum[T]].withName
      )
}
