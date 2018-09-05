package tmt.assembly.client

import csw.messages.commands.ControlCommand
import tmt.WebGateway
import tmt.assembly.api.AssemblyCommandWeb
import tmt.sequencer.codecs.SequencerRWSupport
import tmt.sequencer.models._

import scala.concurrent.Future

class AssemblyCommandWebClient(gateway: WebGateway) extends AssemblyCommandWeb with SequencerRWSupport {

  override def submit(controlCommand: ControlCommand): Future[CommandResponseWeb] = gateway.post(
    url = s"${AssemblyCommandWeb.Submit}",
    data = upickle.default.write(controlCommand),
    transform = x => upickle.default.read[CommandResponseWeb](x)
  )
}
