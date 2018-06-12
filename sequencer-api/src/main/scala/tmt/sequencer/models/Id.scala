package tmt.sequencer.models

import tmt.sequencer.models.StepStatus.{Finished, InFlight, Pending}

case class Step(command: Command, status: StepStatus, hasBreakpoint: Boolean) {
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
  def from(command: Command)                    = Step(command, StepStatus.Pending, hasBreakpoint = false)
  def from(commands: List[Command]): List[Step] = commands.map(command => from(command))
}

sealed trait StepStatus

object StepStatus {
  case object Pending  extends StepStatus
  case object InFlight extends StepStatus
  case object Finished extends StepStatus
}

case class Id(value: String) extends AnyVal {
  override def toString: String = value
}

case class Command(runId: Id, commandName: String, prefix: String, maybeObsId: Option[String] = None) {
  def withId(_runId: Id): Command             = copy(runId = _runId)
  def withName(_commandName: String): Command = copy(commandName = _commandName)
  def withPrefix(_prefix: String): Command    = copy(prefix = _prefix)
  def withObsId(_obsId: String): Command      = copy(maybeObsId = Some(_obsId))

  override def toString: String =
    s"(runId=$runId, commandName=$commandName, maybeObsId=$maybeObsId)"
}

case class CommandList(commands: Seq[Command]) {
  def add(others: Command*): CommandList   = CommandList(commands ++ others)
  def add(other: CommandList): CommandList = CommandList(commands ++ other.commands)
}

object CommandList {
  def from(commands: Command*): CommandList = CommandList(commands.toList)
  def empty: CommandList                    = CommandList(Nil)
}

sealed trait CommandResponse {
  def typeName: String
  def id: Id
  def value: String
}

object CommandResponse {
  case class Success(id: Id, value: String, typeName: String = Success.getClass.getSimpleName) extends CommandResponse
  case class Failed(id: Id, value: String, typeName: String = Failed.getClass.getSimpleName)   extends CommandResponse
}

case class AggregateResponse(childResponses: Set[CommandResponse]) {
  def ids: Set[Id]                                                 = childResponses.map(_.id)
  def add(commandResponses: CommandResponse*): AggregateResponse   = copy(childResponses ++ commandResponses.toSet)
  def add(maybeResponse: Set[CommandResponse]): AggregateResponse  = copy(childResponses ++ maybeResponse)
  def add(aggregateResponse: AggregateResponse): AggregateResponse = copy(childResponses ++ aggregateResponse.childResponses)
  def markSuccessful(commands: Command*): AggregateResponse = add {
    commands.map(command => CommandResponse.Success(id = command.runId, value = "all children are done")).toSet[CommandResponse]
  }
  def markSuccessful(maybeCommand: Option[Command]): AggregateResponse = markSuccessful(maybeCommand.toList: _*)
}

object AggregateResponse extends AggregateResponse(Set.empty)
