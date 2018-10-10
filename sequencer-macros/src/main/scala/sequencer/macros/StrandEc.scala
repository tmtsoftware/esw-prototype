package sequencer.macros

import java.util.concurrent.{Executors, ScheduledExecutorService}

import scala.concurrent.ExecutionContext

class StrandEc(val executorService: ScheduledExecutorService) {
  val ec: ExecutionContext = ExecutionContext.fromExecutorService(executorService)
  def shutdown(): Unit     = executorService.shutdownNow()
}

object StrandEc {
  def create() = new StrandEc(Executors.newSingleThreadScheduledExecutor())
}
