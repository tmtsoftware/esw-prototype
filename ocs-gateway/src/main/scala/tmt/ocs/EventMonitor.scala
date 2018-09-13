package tmt.ocs

import akka.stream.scaladsl.Source
import csw.messages.events.{Event, EventKey, EventName}
import csw.messages.params.models.{Prefix, Subsystem}
import csw.services.event.api.scaladsl.{EventService, EventSubscription}

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
