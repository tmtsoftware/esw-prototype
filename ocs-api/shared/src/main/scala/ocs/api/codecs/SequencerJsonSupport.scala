package ocs.api.codecs

import csw.params.extensions.Formats
import play.api.libs.json._
import ocs.api.models._

trait SequencerJsonSupport extends AssemblyJsonSupport {
  implicit lazy val stepStatusFormat: Format[StepStatus]                      = Formats.enumFormat
  implicit lazy val aggregateResponseFormat: OFormat[AggregateResponse]       = Json.format
  implicit lazy val commandListFormat: OFormat[CommandList]                   = Json.format
  implicit lazy val commandsWithTargetIdFormat: OFormat[CommandsWithTargetId] = Json.format
  implicit lazy val stepFormat: OFormat[Step]                                 = Json.format
  implicit lazy val sequenceFormat: OFormat[Sequence]                         = Json.format
}
