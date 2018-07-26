package tmt.sequencer.api

import tmt.sequencer.models.{AggregateResponseWeb, CommandListWeb}

import scala.concurrent.Future

trait SequenceFeederWeb {
  def feed(commandListWeb: CommandListWeb): Future[Unit]
}

object SequenceFeederWeb {
  val ApiName = "feeder"
  val Feed    = "feed"
}
