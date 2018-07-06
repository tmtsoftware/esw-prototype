package tmt.sequencer.rpc.server

import tmt.sequencer.api.{SequenceFeeder, SequenceFeederWeb}
import tmt.sequencer.models.{AggregateResponse, AggregateResponseWeb, CommandList, CommandListWeb}

import scala.concurrent.{ExecutionContext, Future}

class SequenceFeederWebImpl(sequencerFeeder: SequenceFeeder)(implicit ec: ExecutionContext) extends SequenceFeederWeb {
  override def feed(commandListWeb: CommandListWeb): Future[AggregateResponseWeb] =
    sequencerFeeder
      .feed(CommandList.fromCommandListWeb(commandListWeb))
      .map(AggregateResponse.toAggregateResponseWeb)
}
