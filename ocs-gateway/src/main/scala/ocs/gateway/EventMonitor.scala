package ocs.gateway

import akka.stream.scaladsl.Source
import csw.event.api.scaladsl.{EventService, EventSubscription}
import csw.params.core.models.{Prefix, Subsystem}
import csw.params.events.{Event, EventKey, EventName}

class EventMonitor(eventService: EventService) {
  def subscribe(subsystem: String, component: Option[String], event: Option[String]): Source[Event, EventSubscription] = {
    (component, event) match {
      case (Some(componentVal), Some(eventVal)) =>
        eventService.defaultSubscriber.subscribe(Set(EventKey(Prefix(s"$subsystem.$componentVal"), EventName(eventVal))))
      case (Some(componentVal), None) =>
        eventService.defaultSubscriber.pSubscribe(Subsystem.withName(subsystem), s"$componentVal*")
      case _ =>
        eventService.defaultSubscriber.pSubscribe(Subsystem.withName(subsystem), "*")
    }
  }
}
