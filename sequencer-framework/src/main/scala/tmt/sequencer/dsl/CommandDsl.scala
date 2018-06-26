package tmt.sequencer.dsl

import csw.messages.commands.SequenceCommand
import org.tmt.macros.StrandEc
import tmt.sequencer.Sequencer
import tmt.sequencer.models.AggregateResponse

import scala.concurrent.Future

abstract class CommandDsl(sequencer: Sequencer) extends ScriptDsl {
  val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[AggregateResponse]] = new FunctionBuilder

  def handleCommand(name: String)(handler: SequenceCommand => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler(_.commandName.name == name)(handler)
  }

  def nextIf(f: SequenceCommand => Boolean)(implicit strandEc: StrandEc): Future[Option[SequenceCommand]] =
    spawn {
      val hasNext = sequencer.maybeNext.await.map(_.command).exists(f)
      if (hasNext) Some(sequencer.next.await.command) else None
    }
}
