package ocs.framework.dsl

import akka.Done
import csw.params.commands.CommandResponse.SubmitResponse
import csw.params.commands.{CommandResponse, Observe, SequenceCommand, Setup}
import sequencer.macros.StrandEc

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

//constructor takes cswService instead of sequencer so that it is a bit easier for script-writer to pass as an argument to super
class Script(val csw: CswServices) extends ScriptDsl

trait ScriptDsl extends ControlDsl {

  def csw: CswServices

  protected def nextIf(f: SequenceCommand => Boolean)(implicit strandEc: StrandEc): Future[Option[SequenceCommand]] =
    spawn {
      val hasNext = csw.sequenceOperator.maybeNext.await.map(_.command).exists(f)
      if (hasNext) Some(csw.sequenceOperator.next.await.command) else None
    }

  protected val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[Unit]] = new FunctionBuilder
  protected val shutdownHandlers: FunctionHandlers[Unit, Future[Unit]]                = new FunctionHandlers
  protected val abortHandlers: FunctionHandlers[Unit, Future[Unit]]                   = new FunctionHandlers
  protected val diagHandlers: FunctionHandlers[String, Future[Unit]]                  = new FunctionHandlers

  private lazy val commandHandler: SequenceCommand => Future[Unit] = commandHandlerBuilder.build { input =>
    println(s"unknown command=$input")
    spawn(Done)
  }

  def execute(command: SequenceCommand): Future[Unit] = spawn(commandHandler(command).await)
  def executeDiag(hint: String): Future[Unit]         = par(diagHandlers.execute(hint).toList)

  def callback: SubmitResponse => Unit = response => CommandResponse.isFinal(response)

  def shutdown(): Future[Unit] = spawn {
    onShutdown().await
    strandEc.shutdown()
  }

  private implicit val ec: ExecutionContext = strandEc.ec
  protected def onShutdown(): Future[Unit]  = par(shutdownHandlers.execute(()).toList)
  def abort(): Future[Unit]                 = par(abortHandlers.execute(()).toList)

  private def handle[T <: SequenceCommand: ClassTag](name: String)(handler: T => Future[Unit]): Unit = {
    commandHandlerBuilder.addHandler[T](handler)(_.commandName.name == name)
  }

  protected def handleSetupCommand(name: String)(handler: Setup => Future[Unit]): Unit     = handle(name)(handler)
  protected def handleObserveCommand(name: String)(handler: Observe => Future[Unit]): Unit = handle(name)(handler)
  protected def handleDiagnosticCommand(handler: String => Future[Unit]): Unit             = diagHandlers.add(handler)
  protected def handleShutdown(handler: => Future[Unit]): Unit                             = shutdownHandlers.add(_ => handler)
  protected def handleAbort(handler: => Future[Unit]): Unit                                = abortHandlers.add(_ => handler)
}
