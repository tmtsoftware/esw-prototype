package tmt.assembly.client

import csw.messages.commands.{CommandResponse, ControlCommand}
import tmt.WebGateway
import tmt.assembly.api.AssemblyFeeder
import tmt.assembly.codecs.AssemblyJsonSupport

import scala.concurrent.Future

class AssemblyFeederJsClient(gateway: WebGateway) extends AssemblyFeeder with AssemblyJsonSupport {
  override def submit(controlCommand: ControlCommand): Future[CommandResponse] = gateway.post[ControlCommand, CommandResponse](
    s"${AssemblyFeeder.Submit}",
    controlCommand
  )
}
