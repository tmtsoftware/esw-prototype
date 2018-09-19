package tmt.ocs.dsl

import akka.Done
import csw.params.commands.SequenceCommand
import tmt.ocs.models.AggregateResponse

import scala.concurrent.Future

//constructor takes cswService instead of sequencer so that it is a bit easier for script-writer to pass as an argument to super
abstract class Script(csw: CswServices) extends CommandDsl(csw.sequencer) {

  private lazy val commandHandler: SequenceCommand => Future[AggregateResponse] = commandHandlerBuilder.build { input =>
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
