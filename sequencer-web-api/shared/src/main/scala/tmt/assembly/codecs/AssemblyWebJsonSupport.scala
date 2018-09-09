package tmt.assembly.codecs

import julienrf.json.derived
import play.api.libs.json.{__, Json, OFormat}
import tmt.assembly.models.{PositionResponse, RequestComponent}

trait AssemblyWebJsonSupport {
  implicit val requestComponentFormat: OFormat[RequestComponent] = derived.flat.oformat((__ \ "type").format[String])
  implicit val positionResponseFormat: OFormat[PositionResponse] = Json.format
}
