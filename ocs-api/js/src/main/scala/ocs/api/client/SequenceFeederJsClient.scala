package ocs.api.client

import ocs.api.codecs.SequencerJsonSupport
import ocs.api.models.Sequence
import ocs.api.{SequenceFeeder, WebGateway}

import scala.concurrent.Future

class SequenceFeederJsClient(gateway: WebGateway) extends SequencerJsonSupport {
  def feed(commandList: Sequence): Future[Unit] = gateway.postOneway(
    s"${SequenceFeeder.ApiName}/${SequenceFeeder.Feed}",
    commandList
  )
}
