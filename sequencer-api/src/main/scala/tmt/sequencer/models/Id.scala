package tmt.sequencer.models

import ai.x.play.json.Jsonx
import csw.messages.commands.{CommandResponse, SequenceCommand}
import csw.messages.params.models.Id
import tmt.sequencer.models.StepStatus.{Finished, InFlight, Pending}
import play.api.libs.json._
import csw.messages.params.formats.JsonSupport._

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

  implicit val format: OFormat[Step] = Json.format[Step]
}

sealed trait StepStatus

object StepStatus {

  case object Pending extends StepStatus

  case object InFlight extends StepStatus

  case object Finished extends StepStatus

  import ai.x.play.json.implicits.formatSingleton
  import ai.x.play.json.SingletonEncoder.simpleName
  implicit lazy val jsonFormat: Format[StepStatus] = Jsonx.formatSealed[StepStatus]
}

case class CommandList(commands: Seq[SequenceCommand]) {
  def add(others: SequenceCommand*): CommandList = CommandList(commands ++ others)
  def add(other: CommandList): CommandList       = CommandList(commands ++ other.commands)
}

object CommandList {
  def from(commands: SequenceCommand*): CommandList = CommandList(commands.toList)
  def empty: CommandList                            = CommandList(Nil)

  implicit val format: OFormat[CommandList] = Json.format[CommandList]
}

case class AggregateResponse(childResponses: Set[CommandResponse]) {
  def ids: Set[Id]                                                 = childResponses.map(_.runId)
  def add(commandResponses: CommandResponse*): AggregateResponse   = copy(childResponses ++ commandResponses.toSet)
  def add(maybeResponse: Set[CommandResponse]): AggregateResponse  = copy(childResponses ++ maybeResponse)
  def add(aggregateResponse: AggregateResponse): AggregateResponse = copy(childResponses ++ aggregateResponse.childResponses)
  def markSuccessful(commands: SequenceCommand*): AggregateResponse = add {
    commands.map(command => CommandResponse.Completed(command.runId)).toSet[CommandResponse]
  }
  def markSuccessful(maybeCommand: Option[SequenceCommand]): AggregateResponse = markSuccessful(maybeCommand.toList: _*)
}

object AggregateResponse extends AggregateResponse(Set.empty) {
  implicit val format: OFormat[AggregateResponse] = Json.format[AggregateResponse]
}
