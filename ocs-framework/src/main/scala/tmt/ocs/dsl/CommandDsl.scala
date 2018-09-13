package tmt.ocs.dsl

import csw.messages.commands.{Observe, SequenceCommand, Setup}
import org.tmt.macros.StrandEc
import tmt.ocs.Sequencer
import tmt.ocs.models.AggregateResponse

import scala.concurrent.Future
import scala.reflect.ClassTag

abstract class CommandDsl(sequencer: Sequencer) extends ScriptDsl {
  val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[AggregateResponse]] = new FunctionBuilder

  private def handle[T <: SequenceCommand: ClassTag](name: String)(handler: T => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler[T](handler)(_.commandName.name == name)
  }

  def handleCommand(name: String)(handler: SequenceCommand => Future[AggregateResponse]): Unit = handle(name)(handler)
  def handleSetupCommand(name: String)(handler: Setup => Future[AggregateResponse]): Unit      = handle(name)(handler)
  def handleObserveCommand(name: String)(handler: Observe => Future[AggregateResponse]): Unit  = handle(name)(handler)

  def nextIf(f: SequenceCommand => Boolean)(implicit strandEc: StrandEc): Future[Option[SequenceCommand]] =
    spawn {
      val hasNext = sequencer.maybeNext.await.map(_.command).exists(f)
      if (hasNext) Some(sequencer.next.await.command) else None
    }
}
