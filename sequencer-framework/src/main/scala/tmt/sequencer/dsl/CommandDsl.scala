package tmt.sequencer.dsl

import csw.messages.commands.{Observe, SequenceCommand, Setup}
import org.tmt.macros.StrandEc
import tmt.sequencer.Sequencer
import tmt.sequencer.models.AggregateResponse

import scala.concurrent.Future

abstract class CommandDsl(sequencer: Sequencer) extends ScriptDsl {
  val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[AggregateResponse]] = new FunctionBuilder

  def handleCommand(name: String)(handler: SequenceCommand => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler[SequenceCommand](_.commandName.name == name)(handler)
  }

  def handleSetupCommand(name: String)(handler: Setup => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler[Setup](_.commandName.name == name)(handler)
  }

  def handleObserveCommand(name: String)(handler: Observe => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler[Observe](_.commandName.name == name)(handler)
  }

  def nextIf(f: SequenceCommand => Boolean)(implicit strandEc: StrandEc): Future[Option[SequenceCommand]] =
    spawn {
      val hasNext = sequencer.maybeNext.await.map(_.command).exists(f)
      if (hasNext) Some(sequencer.next.await.command) else None
    }
}
