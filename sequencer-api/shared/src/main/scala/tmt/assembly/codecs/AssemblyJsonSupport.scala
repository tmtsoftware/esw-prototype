package tmt.assembly.codecs

import csw.messages.commands.{CommandIssue, CommandResponse}
import julienrf.json.derived
import play.api.libs.json.{__, Json, OFormat}
import tmt.assembly.models.{PositionResponse, RequestComponent}

trait AssemblyJsonSupport {
  implicit lazy val commandIssueFormat: OFormat[CommandIssue]         = derived.flat.oformat((__ \ "type").format[String])
  implicit lazy val commandResponseFormat: OFormat[CommandResponse]   = derived.flat.oformat((__ \ "type").format[String])
  implicit lazy val requestComponentFormat: OFormat[RequestComponent] = derived.flat.oformat((__ \ "type").format[String])
  implicit lazy val positionResponseFormat: OFormat[PositionResponse] = Json.format
}
