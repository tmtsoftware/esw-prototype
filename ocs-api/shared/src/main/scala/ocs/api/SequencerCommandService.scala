package ocs.api

import ocs.api.models.{AggregateResponse, Sequence}

import scala.concurrent.Future

trait SequencerCommandService {
  def submit(commandList: Sequence): Future[AggregateResponse]
}

object SequencerCommandService {
  val ApiName = "feeder"
  val Feed    = "feed"
}
