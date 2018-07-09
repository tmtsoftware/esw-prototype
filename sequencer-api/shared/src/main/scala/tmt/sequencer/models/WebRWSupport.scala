package tmt.sequencer.models

import play.api.libs.json.{JsObject, Json}
import tmt.sequencer.utils.EnumUpickleSupport
import ujson.Js
import upickle.default.{macroRW, ReadWriter => RW, _}

trait WebRWSupport {
  implicit val jsObjectRW: RW[JsObject] = readwriter[Js.Obj].bimap(
    x => read[Js.Obj](x.toString()),
    x => Json.parse(x.toString()).as[JsObject]
  )
  implicit lazy val aggregateResponseWebRW: RW[AggregateResponseWeb] = macroRW[AggregateResponseWeb]
  implicit lazy val commandListWebRW: RW[CommandListWeb]             = macroRW[CommandListWeb]
  implicit lazy val commandResponseWebRW: RW[CommandResponseWeb]     = macroRW[CommandResponseWeb]
  implicit lazy val sequenceCommandWebRW: RW[SequenceCommandWeb]     = macroRW[SequenceCommandWeb]
  implicit lazy val sequenceWebRW: RW[SequenceWeb]                   = macroRW[SequenceWeb]
  implicit lazy val stepStatusRW: RW[StepStatus]                     = EnumUpickleSupport.enumFormat
  implicit lazy val stepWebRW: RW[StepWeb]                           = macroRW[StepWeb]
}
