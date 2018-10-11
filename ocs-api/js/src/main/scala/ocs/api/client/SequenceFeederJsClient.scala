package ocs.api.client

import ocs.api.codecs.SequencerJsonSupport
import ocs.api.models.{AggregateResponse, CommandList}
import ocs.api.{SequenceFeeder, WebGateway}

import scala.concurrent.Future

class SequenceFeederJsClient(gateway: WebGateway) extends SequenceFeeder with SequencerJsonSupport {
  override def feed(commandList: CommandList): Future[Unit] = gateway.postOneway(
    s"${SequenceFeeder.ApiName}/${SequenceFeeder.Feed}",
    commandList
  )

  override def submit(commandList: CommandList): Future[AggregateResponse] = ???
}