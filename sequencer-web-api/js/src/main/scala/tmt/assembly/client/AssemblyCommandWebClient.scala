package tmt.assembly.client

import tmt.WebGateway
import tmt.assembly.api.AssemblyCommandWeb
import tmt.sequencer.models._

import scala.concurrent.Future

class AssemblyCommandWebClient(gateway: WebGateway) extends AssemblyCommandWeb with WebRWSupport {

  override def submit(controlCommand: ControlCommandWeb): Future[CommandResponseWeb] = gateway.post(
    url = s"${AssemblyCommandWeb.Submit}",
    data = upickle.default.write(controlCommand),
    transform = x => upickle.default.read[CommandResponseWeb](x)
  )
}
