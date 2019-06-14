package ocs.framework.core

import akka.Done
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import csw.params.commands.CommandResponse.Error
import ocs.framework.dsl.Script

import scala.async.Async._
import scala.concurrent.Future
import scala.util.control.NonFatal

class Engine(implicit mat: Materializer) {
  import mat.executionContext

  def start(sequenceOperator: SequenceOperator, script: Script): Future[Done] = {
    Source.repeat(()).mapAsync(1)(_ => processStep(sequenceOperator, script)).runForeach(_ => ())
  }

  def processStep(sequenceOperator: SequenceOperator, script: Script): Future[Done] = async {
    val step = await(sequenceOperator.next)
    script.execute(step.command).recover {
      case NonFatal(e) ⇒
        e.printStackTrace()
        sequenceOperator.update(Error(step.command.runId, e.getMessage))
    }

    await(sequenceOperator.readyToExecuteNext)
  }
}
