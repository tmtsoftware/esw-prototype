package tmt.sequencer.api

import tmt.sequencer.models.{SequenceCommandWeb, SequenceWeb}

import scala.concurrent.Future

trait SequenceEditorWeb {
  def addAll(commands: List[SequenceCommandWeb]): Future[String]
  def pause(): Future[String]
  def resume(): Future[String]
  def reset(): Future[String]
  def sequenceWeb: Future[SequenceWeb]
  def delete(ids: List[String]): Future[String]
  def addBreakpoints(ids: List[String]): Future[String]
  def removeBreakpoints(ids: List[String]): Future[String]
  def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[String]
  def prepend(commands: List[SequenceCommandWeb]): Future[String]
  def replace(id: String, commands: List[SequenceCommandWeb]): Future[String]
  def shutdown(): Future[String]
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
