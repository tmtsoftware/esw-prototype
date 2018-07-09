package tmt.sequencer.models

import play.api.libs.json.{JsArray, JsObject}
import tmt.sequencer.utils.EnumUpickleSupport
import upickle.default.{macroRW, ReadWriter => RW}

trait WebRWSupport {
  implicit lazy val jsObjectRW: RW[JsObject]                         = UpickleFormatAdapter.playJsonToUpickle
  implicit lazy val jsArrayRW: RW[JsArray]                           = UpickleFormatAdapter.playJsonToUpickle
  implicit lazy val aggregateResponseWebRW: RW[AggregateResponseWeb] = macroRW[AggregateResponseWeb]
  implicit lazy val commandListWebRW: RW[CommandListWeb]             = macroRW[CommandListWeb]
  implicit lazy val commandResponseWebRW: RW[CommandResponseWeb]     = macroRW[CommandResponseWeb]
  implicit lazy val sequenceCommandWebRW: RW[SequenceCommandWeb]     = macroRW[SequenceCommandWeb]
  implicit lazy val sequenceWebRW: RW[SequenceWeb]                   = macroRW[SequenceWeb]
  implicit lazy val stepStatusRW: RW[StepStatus]                     = EnumUpickleSupport.enumFormat
  implicit lazy val stepWebRW: RW[StepWeb]                           = macroRW[StepWeb]
}
