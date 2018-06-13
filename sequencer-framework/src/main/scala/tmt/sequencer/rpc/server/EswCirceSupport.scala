package tmt.sequencer.rpc.server

import csw.messages.commands.SequenceCommand
import csw.messages.params.formats.JsonSupport
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import play.api.libs.json
import play.api.libs.json.{JsSuccess, Reads, Writes}

trait EswCirceSupport {

  implicit def enc[A: Writes]: Encoder[A] = {
    Encoder.encodeString.contramap(a => implicitly[Writes[A]].writes(a).toString())
  }
  implicit def dec[A: Reads]: Decoder[A] = {
    Decoder.decodeString.map(a => implicitly[Reads[A]].reads(json.Json.parse(a)).get)
  }

  type A = SequenceCommand

  implicit val sequenceCommandReads: Reads[SequenceCommand]   = Reads(x => JsSuccess(JsonSupport.readSequenceCommand(x)))
  implicit val sequenceCommandWrites: Writes[SequenceCommand] = Writes(x => JsonSupport.writeSequenceCommand(x))

  implicitly[Reads[SequenceCommand]]

}
