package tmt.sequencer.api

import akka.Done
import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id
import tmt.sequencer.models.Sequence

import scala.concurrent.Future

trait SequenceEditor {
  def addAll(commands: List[SequenceCommand]): Future[Unit]
  def pause(): Future[Unit]
  def resume(): Future[Unit]
  def reset(): Future[Unit]
  def sequence: Future[Sequence]
  def isAvailable: Future[Boolean]
  def delete(ids: List[Id]): Future[Unit]
  def addBreakpoints(ids: List[Id]): Future[Unit]
  def removeBreakpoints(ids: List[Id]): Future[Unit]
  def insertAfter(id: Id, commands: List[SequenceCommand]): Future[Unit]
  def prepend(commands: List[SequenceCommand]): Future[Unit]
  def replace(id: Id, commands: List[SequenceCommand]): Future[Unit]
  def shutdown(): Future[Done]
}
