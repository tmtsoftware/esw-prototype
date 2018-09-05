package tmt.sequencer.codecs

import csw.messages.commands.ControlCommand
import tmt.sequencer.models._
import tmt.utils.{EnumUpickleSupport, UpickleFormatAdapter}
import upickle.default.{ReadWriter => RW, _}

trait SequencerRWSupport {
  import csw.messages.params.formats.JsonSupport._

  //WebRWSupport
  implicit lazy val aggregateResponseWebRW: RW[AggregateResponseWeb] = macroRW[AggregateResponseWeb]
  implicit lazy val commandListWebRW: RW[CommandListWeb]             = macroRW[CommandListWeb]
  implicit lazy val commandResponseWebRW: RW[CommandResponseWeb]     = macroRW[CommandResponseWeb]
  implicit lazy val sequenceCommandWebRW: RW[SequenceCommandWeb]     = macroRW[SequenceCommandWeb]
  implicit lazy val sequenceWebRW: RW[SequenceWeb]                   = macroRW[SequenceWeb]
  implicit lazy val stepStatusRW: RW[StepStatus]                     = EnumUpickleSupport.enumFormat
  implicit lazy val stepWebRW: RW[StepWeb]                           = macroRW[StepWeb]

  //UpickleRWSupport
  implicit lazy val controlCommandRW: RW[ControlCommand] = UpickleFormatAdapter.playJsonToUpickle
}
