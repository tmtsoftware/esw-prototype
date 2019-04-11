package ocs.api.client

import ocs.api.codecs.SequencerJsonSupport
import ocs.api.models.Sequence
import ocs.api.{SequencerCommandService, WebGateway}

import scala.concurrent.Future

class SequencerCommandServiceJsClient(gateway: WebGateway) extends SequencerJsonSupport {
  def feed(sequence: Sequence): Future[Unit] = gateway.postOneway(
    s"${SequencerCommandService.ApiName}/${SequencerCommandService.Feed}",
    sequence
  )
}
