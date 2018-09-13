package tmt.ocs.dsl

import akka.Done
import csw.messages.commands.SequenceCommand
import org.tmt.macros.StrandEc
import tmt.ocs.models.AggregateResponse

import scala.concurrent.Future

abstract class Script(cs: CswServices) extends ScriptDsl {

  override implicit val strandEc: StrandEc = cs.strandEc

  private lazy val commandHandler: SequenceCommand => Future[AggregateResponse] = cs.commandHandlerBuilder.build { input =>
    println(s"unknown command=$input")
    spawn(AggregateResponse.empty)
  }

  def execute(command: SequenceCommand): Future[AggregateResponse] = spawn(commandHandler(command).await)

  def shutdown(): Future[Done] = spawn {
    onShutdown().await
    shutdownEc()
  }

  protected def onShutdown(): Future[Done] = spawn(Done)

  private[tmt] def shutdownEc(): Done = {
    strandEc.shutdown()
    Done
  }
}
