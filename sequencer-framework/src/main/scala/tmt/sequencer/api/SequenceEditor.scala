package tmt.sequencer.api

import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id
import tmt.sequencer.models.Sequence

import scala.concurrent.Future

trait SequenceEditor {
  def addAll(commands: List[SequenceCommand]): Future[String]
  def pause(): Future[String]
  def resume(): Future[String]
  def reset(): Future[String]
  def sequence: Future[Sequence]
  def delete(ids: List[Id]): Future[String]
  def addBreakpoints(ids: List[Id]): Future[String]
  def removeBreakpoints(ids: List[Id]): Future[String]
  def insertAfter(id: Id, commands: List[SequenceCommand]): Future[String]
  def prepend(commands: List[SequenceCommand]): Future[String]
  def replace(id: Id, commands: List[SequenceCommand]): Future[String]
  def shutdown(): Future[String]
}
