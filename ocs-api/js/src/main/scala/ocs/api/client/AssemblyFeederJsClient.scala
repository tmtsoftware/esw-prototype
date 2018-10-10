package ocs.api.client

import csw.params.commands.{CommandResponse, ControlCommand}
import ocs.api.codecs.AssemblyJsonSupport
import ocs.api.{AssemblyFeeder, WebGateway}

import scala.concurrent.Future

class AssemblyFeederJsClient(gateway: WebGateway) extends AssemblyFeeder with AssemblyJsonSupport {
  override def submitAndSubscribe(controlCommand: ControlCommand): Future[CommandResponse] =
    gateway.post[ControlCommand, CommandResponse](
      s"${AssemblyFeeder.SubmitAndSubscribe}",
      controlCommand
    )

  override def submit(controlCommand: ControlCommand): Future[CommandResponse] = gateway.post[ControlCommand, CommandResponse](
    s"${AssemblyFeeder.Submit}",
    controlCommand
  )
}
