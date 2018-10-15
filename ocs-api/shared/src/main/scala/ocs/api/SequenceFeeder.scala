package ocs.api

import ocs.api.models.{AggregateResponse, Sequence}

import scala.concurrent.Future

trait SequenceFeeder {
  def feed(commandList: Sequence): Future[Unit]
  def submit(commandList: Sequence): Future[AggregateResponse]
}

object SequenceFeeder {
  val ApiName = "feeder"
  val Feed    = "feed"
}
