package tmt.sequencer

import scala.concurrent.duration.DurationDouble
import scala.language.implicitConversions

object ScriptImports {

  implicit def toDuration(d: Double): DurationDouble = new DurationDouble(d)

  type Done = akka.Done
  val Done = akka.Done

  type Script      = dsl.Script
  type CswServices = dsl.CswServices

  type SystemEvent = csw.messages.events.SystemEvent
  val SystemEvent = csw.messages.events.SystemEvent

  type ObserveEvent = csw.messages.events.ObserveEvent
  val ObserveEvent = csw.messages.events.ObserveEvent

  type EventName = csw.messages.events.EventName
  val EventName = csw.messages.events.EventName

  type EventKey = csw.messages.events.EventKey
  val EventKey = csw.messages.events.EventKey

  type Setup = csw.messages.commands.Setup
  val Setup = csw.messages.commands.Setup

  type Observe = csw.messages.commands.Observe
  val Observe = csw.messages.commands.Observe

  type Wait = csw.messages.commands.Wait
  val Wait = csw.messages.commands.Wait

  type Prefix = csw.messages.params.models.Prefix
  val Prefix = csw.messages.params.models.Prefix

  type ObsId = csw.messages.params.models.ObsId
  val ObsId = csw.messages.params.models.ObsId

  type CommandName = csw.messages.commands.CommandName
  val CommandName = csw.messages.commands.CommandName

  type CommandResponse = csw.messages.commands.CommandResponse
  val CommandResponse = csw.messages.commands.CommandResponse

  type AggregateResponse = tmt.sequencer.models.AggregateResponse
  val AggregateResponse = tmt.sequencer.models.AggregateResponse

  type CommandList = tmt.sequencer.models.CommandList
  val CommandList = tmt.sequencer.models.CommandList

  type SequenceCommand = csw.messages.commands.SequenceCommand

  type Future[T] = scala.concurrent.Future[T]

  type Id = csw.messages.params.models.Id
  val Id = csw.messages.params.models.Id
}
