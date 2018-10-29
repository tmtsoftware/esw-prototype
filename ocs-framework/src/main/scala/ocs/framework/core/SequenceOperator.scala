package ocs.framework.core

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.{ActorSystem, Scheduler}
import akka.util.Timeout
import csw.params.commands.CommandResponse.SubmitResponse
import ocs.api.messages.SequencerMsg._
import ocs.api.models._

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong

class SequenceOperator(sequencer: ActorRef[InternalSequencerMsg], system: ActorSystem) {
  private implicit val timeout: Timeout     = Timeout(10.hour)
  private implicit val scheduler: Scheduler = system.scheduler

  def next: Future[Step]                     = sequencer ? GetNext
  def maybeNext: Future[Option[Step]]        = sequencer ? MaybeNext
  def update(response: SubmitResponse): Unit = sequencer ! Update(response)
  def canExecuteNext: Future[Unit]           = sequencer ? CanExecuteNext
}
