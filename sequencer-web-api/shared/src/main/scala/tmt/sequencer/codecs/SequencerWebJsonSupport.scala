package tmt.sequencer.codecs

import csw.messages.extensions.Formats
import play.api.libs.json.{Format, Json, OFormat}
import tmt.sequencer.models._

trait SequencerWebJsonSupport {
  implicit lazy val aggregateResponseWebFormat: OFormat[AggregateResponseWeb] = Json.format
  implicit lazy val commandListWebFormat: OFormat[CommandListWeb]             = Json.format
  implicit lazy val commandResponseWebFormat: OFormat[CommandResponseWeb]     = Json.format
  implicit lazy val sequenceCommandWebFormat: OFormat[SequenceCommandWeb]     = Json.format
  implicit lazy val sequenceWebFormat: OFormat[SequenceWeb]                   = Json.format
  implicit lazy val stepStatusFormat: Format[StepStatus]                      = Formats.enumFormat
  implicit lazy val stepWebFormat: OFormat[StepWeb]                           = Json.format
}
