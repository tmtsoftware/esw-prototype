package tmt.ocs.api

import tmt.ocs.models.{AggregateResponse, CommandList}

import scala.concurrent.Future

trait SequenceFeeder {
  def feed(commandList: CommandList): Future[Unit]
  def submit(commandList: CommandList): Future[AggregateResponse]
}

object SequenceFeeder {
  val ApiName = "feeder"
  val Feed    = "feed"
}
