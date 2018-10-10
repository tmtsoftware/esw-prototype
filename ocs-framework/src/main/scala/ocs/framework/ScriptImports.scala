package ocs.framework

import scala.concurrent.duration.DurationDouble
import scala.language.implicitConversions

object ScriptImports {

  implicit def toDuration(d: Double): DurationDouble = new DurationDouble(d)

  type Done = akka.Done
  val Done = akka.Done

  type Script      = dsl.Script
  type CswServices = dsl.CswServices

  type SystemEvent = csw.params.events.SystemEvent
  val SystemEvent = csw.params.events.SystemEvent

  type ObserveEvent = csw.params.events.ObserveEvent
  val ObserveEvent = csw.params.events.ObserveEvent

  type EventName = csw.params.events.EventName
  val EventName = csw.params.events.EventName

  type EventKey = csw.params.events.EventKey
  val EventKey = csw.params.events.EventKey

  type Setup = csw.params.commands.Setup
  val Setup = csw.params.commands.Setup

  type Observe = csw.params.commands.Observe
  val Observe = csw.params.commands.Observe

  type Wait = csw.params.commands.Wait
  val Wait = csw.params.commands.Wait

  type Prefix = csw.params.core.models.Prefix
  val Prefix = csw.params.core.models.Prefix

  type ObsId = csw.params.core.models.ObsId
  val ObsId = csw.params.core.models.ObsId

  type CommandName = csw.params.commands.CommandName
  val CommandName = csw.params.commands.CommandName

  type CommandResponse = csw.params.commands.CommandResponse
  val CommandResponse = csw.params.commands.CommandResponse

  type AggregateResponse = ocs.api.models.AggregateResponse
  val AggregateResponse = ocs.api.models.AggregateResponse

  type CommandList = ocs.api.models.CommandList
  val CommandList = ocs.api.models.CommandList

  type SequenceCommand = csw.params.commands.SequenceCommand

  type Future[T] = scala.concurrent.Future[T]

  type Id = csw.params.core.models.Id
  val Id = csw.params.core.models.Id
}
