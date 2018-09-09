package tmt.sequencer.models

import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id

case class Sequence(steps: List[Step]) { outer =>

  require(steps.map(_.id).toSet.size == steps.size, "steps can not have duplicate ids")

  //query

  def nextPending: Option[Step] = steps.find(_.isPending)
  def isPaused: Boolean         = nextPending.exists(_.hasBreakpoint)
  def next: Option[Step]        = if (!isPaused) nextPending else None
  def isFinished: Boolean       = steps.forall(_.isFinished)

  //update

  def replace(id: Id, commands: List[SequenceCommand]): Sequence = replaceSteps(id, Step.from(commands))
  private def replaceSteps(id: Id, steps: List[Step]): Sequence  = insertStepsAfter(id, steps).delete(Set(id))

  def prepend(commands: List[SequenceCommand]): Sequence = {
    val (pre, post) = steps.span(!_.isPending)
    copy(pre ::: Step.from(commands) ::: post)
  }
  def append(commands: List[SequenceCommand]): Sequence = copy(steps ::: Step.from(commands))

  def delete(ids: Set[Id]): Sequence = copy(steps.filterNot(step => ids.contains(step.id) && step.isPending))

  def insertAfter(id: Id, commands: List[SequenceCommand]): Sequence = insertStepsAfter(id, Step.from(commands))

  private def insertStepsAfter(id: Id, newSteps: List[Step]): Sequence = {
    val (pre, post) = steps.span(_.id != id)
    copy(pre ::: post.headOption.toList ::: newSteps ::: post.tail)
  }

  def discardPending: Sequence = copy(steps.filterNot(_.isPending))

  def addBreakpoints(ids: List[Id]): Sequence    = updateAll(ids.toSet, _.addBreakpoint())
  def removeBreakpoints(ids: List[Id]): Sequence = updateAll(ids.toSet, _.removeBreakpoint())

  def pause: Sequence  = nextPending.map(step => updateStep(step.addBreakpoint())).flat
  def resume: Sequence = nextPending.map(step => updateStep(step.removeBreakpoint())).flat

  def updateStep(step: Step): Sequence                             = updateAll(Set(step.id), _ => step)
  def updateStatus(ids: Set[Id], stepStatus: StepStatus): Sequence = updateAll(ids, _.withStatus(stepStatus))

  def updateAll(ids: Set[Id], f: Step => Step): Sequence = copy {
    steps.map {
      case step if ids.contains(step.id) => f(step)
      case step                          => step
    }
  }

  private implicit class StepOps(optStep: Option[Sequence]) {
    def flat: Sequence = optStep.getOrElse(outer)
  }
}

object Sequence {
  def empty                                 = Sequence(List.empty)
  def from(commands: List[SequenceCommand]) = Sequence(Step.from(commands))
}
