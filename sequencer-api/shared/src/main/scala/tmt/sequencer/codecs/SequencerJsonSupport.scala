package tmt.sequencer.codecs

import play.api.libs.json._
import tmt.sequencer.models._
import csw.messages.extensions.Formats
import tmt.assembly.codecs.AssemblyJsonSupport

trait SequencerJsonSupport extends AssemblyJsonSupport {
  implicit lazy val stepStatusFormat: Format[StepStatus]                = Formats.enumFormat
  implicit lazy val aggregateResponseFormat: OFormat[AggregateResponse] = Json.format
  implicit lazy val commandListFormat: OFormat[CommandList]             = Json.format
  implicit lazy val stepFormat: OFormat[Step]                           = Json.format
  implicit lazy val sequenceFormat: OFormat[Sequence]                   = Json.format
}
