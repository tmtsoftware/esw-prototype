package tmt.sequencer.client

import tmt.WebGateway
import tmt.sequencer.api.SequenceFeeder
import tmt.sequencer.codecs.SequencerJsonSupport
import tmt.sequencer.models.{AggregateResponse, CommandList}

import scala.concurrent.Future

class SequenceFeederJsClient(gateway: WebGateway) extends SequenceFeeder with SequencerJsonSupport {
  override def feed(commandList: CommandList): Future[Unit] = gateway.postOneway(
    s"${SequenceFeeder.ApiName}/${SequenceFeeder.Feed}",
    commandList
  )

  override def submit(commandList: CommandList): Future[AggregateResponse] = ???
}
