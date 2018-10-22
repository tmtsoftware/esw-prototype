package ocs.framework.dsl

import akka.Done
import akka.util.Timeout
import csw.params.commands.CommandResponse.SubmitResponse
import csw.params.commands.{CommandResponse, Observe, SequenceCommand, Setup}
import sequencer.macros.StrandEc

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.reflect.ClassTag

//constructor takes cswService instead of sequencer so that it is a bit easier for script-writer to pass as an argument to super
abstract class Script(csw: CswServices) extends ControlDsl {

  protected def nextIf(f: SequenceCommand => Boolean)(implicit strandEc: StrandEc): Future[Option[SequenceCommand]] =
    spawn {
      val hasNext = csw.sequencer.maybeNext.await.map(_.command).exists(f)
      if (hasNext) Some(csw.sequencer.next.await.command) else None
    }

  protected val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[Done]] = new FunctionBuilder

  private lazy val commandHandler: SequenceCommand => Future[Done] = commandHandlerBuilder.build { input =>
    println(s"unknown command=$input")
    spawn(Done)
  }

  def execute(command: SequenceCommand): Future[SubmitResponse] = {
    implicit val timeout: Timeout = Timeout(10.seconds)
    csw.addOrUpdateCommand(command.runId, CommandResponse.Started(command.runId))
    spawn(commandHandler(command))
    csw.queryFinalCommandStatus(command.runId)
  }

  def callback: SubmitResponse => Unit = response => CommandResponse.isFinal(response)

  private def handle[T <: SequenceCommand: ClassTag](name: String)(handler: T => Future[Done]): Unit = {
    commandHandlerBuilder.addHandler[T](handler)(_.commandName.name == name)
  }

  protected def handleSetupCommand(name: String)(handler: Setup => Future[Done]): Unit     = handle(name)(handler)
  protected def handleObserveCommand(name: String)(handler: Observe => Future[Done]): Unit = handle(name)(handler)
}
