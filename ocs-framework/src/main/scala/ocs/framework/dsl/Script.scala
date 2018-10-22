package ocs.framework.dsl

import csw.params.commands.{Observe, SequenceCommand, Setup}
import ocs.api.models.AggregateResponse
import sequencer.macros.StrandEc

import scala.concurrent.Future
import scala.reflect.ClassTag

//constructor takes cswService instead of sequencer so that it is a bit easier for script-writer to pass as an argument to super
abstract class Script(csw: CswServices) extends ControlDsl {

  protected def nextIf(f: SequenceCommand => Boolean)(implicit strandEc: StrandEc): Future[Option[SequenceCommand]] =
    spawn {
      val hasNext = csw.sequencer.maybeNext.await.map(_.command).exists(f)
      if (hasNext) Some(csw.sequencer.next.await.command) else None
    }

  protected val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[AggregateResponse]] = new FunctionBuilder

  private lazy val commandHandler: SequenceCommand => Future[AggregateResponse] = commandHandlerBuilder.build { input =>
    println(s"unknown command=$input")
    spawn(AggregateResponse())
  }

  def execute(command: SequenceCommand): Future[AggregateResponse] = spawn(commandHandler(command).await)

  private def handle[T <: SequenceCommand: ClassTag](name: String)(handler: T => Future[AggregateResponse]): Unit = {
    commandHandlerBuilder.addHandler[T](handler)(_.commandName.name == name)
  }

  protected def handleSetupCommand(name: String)(handler: Setup => Future[AggregateResponse]): Unit     = handle(name)(handler)
  protected def handleObserveCommand(name: String)(handler: Observe => Future[AggregateResponse]): Unit = handle(name)(handler)
}
