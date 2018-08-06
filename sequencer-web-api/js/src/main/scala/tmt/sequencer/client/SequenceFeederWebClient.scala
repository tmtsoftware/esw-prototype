package tmt.sequencer.client

import tmt.WebGateway
import tmt.sequencer.api.SequenceFeederWeb
import tmt.sequencer.models.{CommandListWeb, WebRWSupport}

import scala.concurrent.Future

class SequenceFeederWebClient(gateway: WebGateway) extends SequenceFeederWeb with WebRWSupport {

  override def feed(commandList: CommandListWeb): Future[Unit] = gateway.post(
    url = s"${SequenceFeederWeb.ApiName}/${SequenceFeederWeb.Feed}",
    data = upickle.default.write(commandList)
  )
}
