package ocs.api.models

import csw.params.commands.{CommandResponse, SequenceCommand}
import csw.params.core.models.Id
import ocs.api.models.StepStatus._

case class Step(command: SequenceCommand, status: StepStatus, hasBreakpoint: Boolean) {
  def id: Id              = command.runId
  def isPending: Boolean  = status == StepStatus.Pending
  def isFinished: Boolean = status == StepStatus.Finished

  def addBreakpoint(): Step    = if (isPending) copy(hasBreakpoint = true) else this
  def removeBreakpoint(): Step = copy(hasBreakpoint = false)

  def withStatus(newStatus: StepStatus): Step = {
    (status, newStatus) match {
      case (Pending, InFlight)  => copy(status = newStatus)
      case (InFlight, Finished) => copy(status = newStatus)
      case _                    => this
    }
  }
}

object Step {
  def from(command: SequenceCommand)                    = Step(command, StepStatus.Pending, hasBreakpoint = false)
  def from(commands: List[SequenceCommand]): List[Step] = commands.map(command => from(command))
}

case class Sequence(runId: Id, commands: Seq[SequenceCommand]) {
  def add(others: SequenceCommand*): Sequence = Sequence(runId, commands ++ others)
  def add(other: Sequence): Sequence          = Sequence(runId, commands ++ other.commands)
}

object Sequence {
  def from(commands: SequenceCommand*): Sequence = Sequence(Id(), commands.toList)
  def empty: Sequence                            = Sequence(Id(), Nil)
}

case class AggregateResponse(childResponses: Set[CommandResponse]) {
  def ids: Set[Id]                                                   = childResponses.map(_.runId)
  def merge(aggregateResponse: AggregateResponse): AggregateResponse = copy(childResponses ++ aggregateResponse.childResponses)
}

object AggregateResponse {
  def apply(commandResponses: CommandResponse*): AggregateResponse = new AggregateResponse(commandResponses.toSet)
}

case class CommandsWithTargetId(targetId: Id, commands: List[SequenceCommand])
