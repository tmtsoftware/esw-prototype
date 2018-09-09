package tmt.assembly.client

import csw.messages.commands.ControlCommand
import play.api.libs.json.Json
import tmt.WebGateway
import tmt.assembly.api.AssemblyCommandWeb
import tmt.sequencer.codecs.SequencerWebJsonSupport
import tmt.sequencer.models._

import scala.concurrent.Future

class AssemblyCommandWebClient(gateway: WebGateway) extends AssemblyCommandWeb with SequencerWebJsonSupport {
  import csw.messages.params.formats.JsonSupport._

  override def submit(controlCommand: ControlCommand): Future[CommandResponseWeb] = gateway.post(
    url = s"${AssemblyCommandWeb.Submit}",
    data = Json.toJson(controlCommand).toString(),
    transform = x => Json.parse(x).as[CommandResponseWeb]
  )
}
