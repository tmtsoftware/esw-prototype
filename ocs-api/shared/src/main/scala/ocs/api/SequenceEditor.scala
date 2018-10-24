package ocs.api

import csw.params.commands.SequenceCommand
import csw.params.core.models.Id
import ocs.api.models.StepList

import scala.concurrent.Future

trait SequenceEditor {
  def addAll(commands: List[SequenceCommand]): Future[Unit]
  def pause(): Future[Unit]
  def resume(): Future[Unit]
  def reset(): Future[Unit]
  def sequence: Future[StepList]
  def isAvailable: Future[Boolean]
  def delete(ids: List[Id]): Future[Unit]
  def addBreakpoints(ids: List[Id]): Future[Unit]
  def removeBreakpoints(ids: List[Id]): Future[Unit]
  def insertAfter(id: Id, commands: List[SequenceCommand]): Future[Unit]
  def prepend(commands: List[SequenceCommand]): Future[Unit]
  def replace(id: Id, commands: List[SequenceCommand]): Future[Unit]
  def shutdown(): Future[Unit]
  def start(): Future[Unit]
  def stop(): Future[Unit]
}

object SequenceEditor {
  val ApiName           = "editor"
  val AddAll            = "addAll"
  val Pause             = "pause"
  val Resume            = "resume"
  val Reset             = "reset"
  val Sequence          = "sequence"
  val Delete            = "delete"
  val AddBreakpoints    = "addBreakpoints"
  val RemoveBreakpoints = "removeBreakpoints"
  val InsertAfter       = "insertAfter"
  val Prepend           = "prepend"
  val Replace           = "replace"
  val Shutdown          = "shutdown"
  val Start             = "start"
  val Stop              = "stop"
}
