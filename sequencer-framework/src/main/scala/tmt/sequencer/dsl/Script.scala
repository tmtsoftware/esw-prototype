package tmt.sequencer.dsl

import akka.Done
import csw.messages.commands.SequenceCommand
import tmt.sequencer.models.AggregateResponse

import scala.concurrent.Future

abstract class Script(cs: CswServices) extends ActiveObject {
  private lazy val commandHandler: SequenceCommand => Future[AggregateResponse] = cs.commandHandlerBuilder.build { input =>
    println(s"unknown command=$input")
    spawn(AggregateResponse)
  }

  def execute(command: SequenceCommand): Future[AggregateResponse] = spawn(commandHandler(command).await)

  def shutdown(): Future[Done] = onShutdown().map(_ => shutdownEc())

  protected def onShutdown(): Future[Done] = spawn(Done)
}
