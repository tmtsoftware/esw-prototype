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

  protected val commandHandlerBuilder: FunctionBuilder[SequenceCommand, Future[Done]] = new FunctionBuilder
  protected val shutdownHandlers: Function0Handlers[Future[Done]]                     = new Function0Handlers
  protected val abortHandlers: Function0Handlers[Future[Done]]                        = new Function0Handlers
  protected val diagHandlers: Function1Handlers[String, Future[Done]]                 = new Function1Handlers

  private lazy val commandHandler: SequenceCommand => Future[Done] = commandHandlerBuilder.build { input =>
    println(s"unknown command=$input")
    spawn(Done)
  }

  def execute(command: SequenceCommand): Future[Done] = spawn(commandHandler(command).await)
  def executeDiag(hint: String): Future[Done] = par(diagHandlers.execute(hint).toList)

  def callback: SubmitResponse => Unit = response => CommandResponse.isFinal(response)

  def shutdown(): Future[Done] = spawn {
    onShutdown().await
    strandEc.shutdown()
    Done
  }

  private implicit val ec: ExecutionContext = strandEc.ec
  protected def onShutdown(): Future[Done]  = Future.sequence(shutdownHandlers.execute()).map(_ ⇒ Done)
  def abort(): Future[Done]                 = Future.sequence(abortHandlers.execute()).map(_ ⇒ Done)

  private def handle[T <: SequenceCommand: ClassTag](name: String)(handler: T => Future[Done]): Unit = {
    commandHandlerBuilder.addHandler[T](handler)(_.commandName.name == name)
  }

  protected def handleSetupCommand(name: String)(handler: Setup => Future[Done]): Unit     = handle(name)(handler)
  protected def handleObserveCommand(name: String)(handler: Observe => Future[Done]): Unit = handle(name)(handler)
  protected def handleDiagnosticCommand(handler: String => Future[Done]): Unit             = diagHandlers.add(handler)
  protected def handleShutdown(handler: => Future[Done]): Unit                             = shutdownHandlers.add(handler)
  protected def handleAbort(handler: => Future[Done]): Unit                                = abortHandlers.add(handler)

  // this is not required, instead of this - use mix-in approach (keeping it for a while for reference.)
  protected def injectHandlers(scripts: Script*): Unit =
    commandHandlerBuilder.handlers ++= scripts.flatMap(_.commandHandlerBuilder.handlers)

}
