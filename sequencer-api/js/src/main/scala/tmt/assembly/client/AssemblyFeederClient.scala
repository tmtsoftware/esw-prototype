package tmt.assembly.client

import csw.messages.commands.{CommandResponse, ControlCommand}
import play.api.libs.json.Json
import tmt.WebGateway
import tmt.assembly.api.AssemblyFeeder
import tmt.sequencer.codecs.SequencerJsonSupport

import scala.concurrent.Future

class AssemblyFeederClient(gateway: WebGateway) extends AssemblyFeeder with SequencerJsonSupport {
  import csw.messages.params.formats.JsonSupport._

  override def submit(controlCommand: ControlCommand): Future[CommandResponse] = gateway.post(
    url = s"${AssemblyFeeder.Submit}",
    data = Json.toJson(controlCommand).toString(),
    transform = x => Json.parse(x).as[CommandResponse]
  )
}
