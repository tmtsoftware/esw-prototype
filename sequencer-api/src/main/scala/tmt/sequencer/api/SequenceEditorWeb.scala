package tmt.sequencer.api

import tmt.sequencer.models.{SequenceCommandWeb, SequenceWeb}

import scala.concurrent.Future

trait SequenceEditorWeb {
  def addAll(commands: List[SequenceCommandWeb]): Future[Unit]
  def pause(): Future[Unit]
  def resume(): Future[Unit]
  def reset(): Future[Unit]
  def sequenceWeb: Future[SequenceWeb]
  def delete(ids: List[String]): Future[Unit]
  def addBreakpoints(ids: List[String]): Future[Unit]
  def removeBreakpoints(ids: List[String]): Future[Unit]
  def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[Unit]
  def prepend(commands: List[SequenceCommandWeb]): Future[Unit]
  def replace(id: String, commands: List[SequenceCommandWeb]): Future[Unit]
  def shutdown(): Future[Unit]
}

object SequenceEditorWeb {
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
}
