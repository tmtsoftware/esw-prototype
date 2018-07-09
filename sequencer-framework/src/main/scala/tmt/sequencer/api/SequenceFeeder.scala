package tmt.sequencer.api

import tmt.sequencer.models.{AggregateResponse, CommandList, CommandListWeb, SequenceCommandConversions}

import scala.concurrent.Future

trait SequenceFeeder {
  def feed(commandList: CommandList): Future[AggregateResponse]
}
