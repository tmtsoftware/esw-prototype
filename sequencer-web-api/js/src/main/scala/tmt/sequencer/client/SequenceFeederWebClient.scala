package tmt.sequencer.client

import play.api.libs.json.Json
import tmt.WebGateway
import tmt.sequencer.api.SequenceFeederWeb
import tmt.sequencer.codecs.SequencerWebJsonSupport
import tmt.sequencer.models.CommandListWeb

import scala.concurrent.Future

class SequenceFeederWebClient(gateway: WebGateway) extends SequenceFeederWeb with SequencerWebJsonSupport {
  override def feed(commandList: CommandListWeb): Future[Unit] = gateway.post(
    url = s"${SequenceFeederWeb.ApiName}/${SequenceFeederWeb.Feed}",
    data = Json.toJson(commandList).toString()
  )
}
