package tmt.assembly.codecs

import tmt.assembly.models.RequestComponent.{Disperser, FilterWheel}
import tmt.assembly.models.{PositionResponse, RequestComponent}
import upickle.default.{ReadWriter => RW, _}

trait AssemblyWebRWSupport {
  implicit lazy val requestComponentWebRW: RW[RequestComponent] = RW.merge(macroRW[FilterWheel], macroRW[Disperser])
  implicit lazy val PositionResponseWebRW: RW[PositionResponse] = macroRW[PositionResponse]
}
