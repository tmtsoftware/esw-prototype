package ocs.framework.dsl.epic

import akka.Done
import akka.stream.Materializer
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal._
import ocs.framework.dsl.epic.internal.event.MockEventService
import sequencer.macros.StrandEc

import scala.concurrent.{ExecutionContext, Future}

trait ProgramContext {
  implicit def strandEc: StrandEc
  implicit def ec: ExecutionContext
  implicit def mat: Materializer
  implicit def eventService: MockEventService
}

trait Refreshable {
  def refresh(source: String): Future[Done]
}

abstract class Program(cswSystem: CswSystem) extends Refreshable { outer =>
  private var machines: List[Machine[_]] = List.empty

  implicit object Context extends ProgramContext {
    implicit lazy val strandEc: StrandEc             = StrandEc.create()
    implicit lazy val ec: ExecutionContext           = strandEc.ec
    implicit lazy val mat: Materializer              = cswSystem.createMaterializer()
    implicit lazy val eventService: MockEventService = cswSystem.mockEventService
  }

  implicit lazy val Refreshable: Refreshable = outer
  import Context._

  def refresh(source: String): Future[Done] = Future.traverse(machines)(_.refresh(source)).map(_ => Done)

  def setup(machine: Machine[_]): Unit = {
    machines ::= machine
  }
}
