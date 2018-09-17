package tmt.ocs.client

import csw.params.commands.{CommandResponse, ControlCommand}
import tmt.ocs.WebGateway
import tmt.ocs.api.AssemblyFeeder
import tmt.ocs.codecs.AssemblyJsonSupport

import scala.concurrent.Future

class AssemblyFeederJsClient(gateway: WebGateway) extends AssemblyFeeder with AssemblyJsonSupport {
  override def submit(controlCommand: ControlCommand): Future[CommandResponse] = gateway.post[ControlCommand, CommandResponse](
    s"${AssemblyFeeder.Submit}",
    controlCommand
  )
}
