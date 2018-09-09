package tmt.sequencer.codecs

import play.api.libs.json.{Format, Json, OFormat}
import tmt.sequencer.models._

import csw.messages.extensions.Formats
import tmt.assembly.codecs.AssemblyJsonSupport

trait SequencerJsonSupport extends AssemblyJsonSupport {
  import csw.messages.params.formats.JsonSupport._
  implicit lazy val stepStatusFormat: Format[StepStatus]                = Formats.enumFormat
  implicit lazy val aggregateResponseFormat: OFormat[AggregateResponse] = Json.format
  implicit lazy val commandListFormat: OFormat[CommandList]             = Json.format
  implicit lazy val stepFormat: OFormat[Step]                           = Json.format
  implicit lazy val sequenceFormat: OFormat[Sequence]                   = Json.format
}
