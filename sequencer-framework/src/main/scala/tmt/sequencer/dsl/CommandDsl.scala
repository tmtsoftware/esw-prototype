package tmt.sequencer.dsl

import csw.messages.commands.SequenceCommand
import tmt.sequencer.Sequencer
import tmt.sequencer.models.AggregateResponse

import scala.async.Async.{async, await}
import scala.concurrent.Future
import scala.util.control.Breaks.breakable

abstract class CommandDsl(sequencer: Sequencer) extends ActiveObject {
  val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[AggregateResponse]] = new FunctionBuilder

  def handleCommand(name: String)(handler: SequenceCommand => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler(_.commandName.name == name)(handler)
  }

  def nextIf(f: SequenceCommand => Boolean): Future[Option[SequenceCommand]] =
    async {
      val hasNext = await(sequencer.maybeNext).map(_.command).exists(f)
      if (hasNext) Some(await(sequencer.next).command) else None
    }

  def loop(body: => Unit): Unit = {
    breakable(
      while (true) {
        body
      }
    )
  }
}
