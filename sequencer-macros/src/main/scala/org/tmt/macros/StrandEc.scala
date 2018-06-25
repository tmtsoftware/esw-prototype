package org.tmt.macros

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

class StrandEc(val ec: ExecutionContextExecutorService) {
  def shutdown(): Unit = ec.shutdown()
}

object StrandEc {
  def create() = new StrandEc(ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor()))
}
