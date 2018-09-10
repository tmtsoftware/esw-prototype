package tmt.sequencer.client

import play.api.libs.json.Json
import tmt.WebGateway
import tmt.sequencer.api.SequenceFeeder
import tmt.sequencer.codecs.SequencerJsonSupport
import tmt.sequencer.models.{AggregateResponse, CommandList}

import scala.concurrent.Future

class SequenceFeederJsClient(gateway: WebGateway) extends SequenceFeeder with SequencerJsonSupport {
  override def feed(commandList: CommandList): Future[Unit] = gateway.post(
    url = s"${SequenceFeeder.ApiName}/${SequenceFeeder.Feed}",
    data = Json.toJson(commandList).toString()
  )

  override def submit(commandList: CommandList): Future[AggregateResponse] = ???
}
