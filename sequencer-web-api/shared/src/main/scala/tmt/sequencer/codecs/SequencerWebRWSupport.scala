package tmt.sequencer.codecs

import tmt.sequencer.models._
import tmt.utils.EnumUpickleSupport
import upickle.default.{ReadWriter => RW, _}

trait SequencerWebRWSupport {
  implicit lazy val aggregateResponseWebRW: RW[AggregateResponseWeb] = macroRW[AggregateResponseWeb]
  implicit lazy val commandListWebRW: RW[CommandListWeb]             = macroRW[CommandListWeb]
  implicit lazy val commandResponseWebRW: RW[CommandResponseWeb]     = macroRW[CommandResponseWeb]
  implicit lazy val sequenceCommandWebRW: RW[SequenceCommandWeb]     = macroRW[SequenceCommandWeb]
  implicit lazy val controlCommandWebRW: RW[ControlCommandWeb]       = macroRW[ControlCommandWeb]
  implicit lazy val sequenceWebRW: RW[SequenceWeb]                   = macroRW[SequenceWeb]
  implicit lazy val stepStatusRW: RW[StepStatus]                     = EnumUpickleSupport.enumFormat
  implicit lazy val stepWebRW: RW[StepWeb]                           = macroRW[StepWeb]
}
