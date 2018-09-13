package tmt.ocs.client

import tmt.ocs.WebGateway
import tmt.ocs.api.SequenceFeeder
import tmt.ocs.codecs.SequencerJsonSupport
import tmt.ocs.models.{AggregateResponse, CommandList}

import scala.concurrent.Future

class SequenceFeederJsClient(gateway: WebGateway) extends SequenceFeeder with SequencerJsonSupport {
  override def feed(commandList: CommandList): Future[Unit] = gateway.postOneway(
    s"${SequenceFeeder.ApiName}/${SequenceFeeder.Feed}",
    commandList
  )

  override def submit(commandList: CommandList): Future[AggregateResponse] = ???
}
