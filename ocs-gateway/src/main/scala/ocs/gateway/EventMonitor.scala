package ocs.gateway

import akka.stream.scaladsl.Source
import csw.event.api.scaladsl.{EventService, EventSubscription, SubscriptionModes}
import csw.params.core.models.{Prefix, Subsystem}
import csw.params.events.{Event, EventKey, EventName}
import scala.concurrent.duration._

class EventMonitor(eventService: EventService) {

  /**
   * Subscribes to events based on the given arguments.
   *
   * @param subsystem subsystem name
   * @param component component name
   * @param event event name
   * @param rateLimit optional rate limit in ms (only used if event name is given)
   */
  def subscribe(subsystem: String,
                component: Option[String],
                event: Option[String],
                rateLimit: Option[Int]): Source[Event, EventSubscription] = {
    val subscriber = eventService.defaultSubscriber
    (component, event) match {
      case (Some(componentVal), Some(eventVal)) =>
        val eventKey = EventKey(Prefix(s"${subsystem.toLowerCase}.$componentVal"), EventName(eventVal))
        rateLimit match {
          case None           => subscriber.subscribe(Set(eventKey))
          case Some(duration) => subscriber.subscribe(Set(eventKey), duration.millis, SubscriptionModes.RateLimiterMode)
        }
      case (Some(componentVal), None) =>
        subscriber.pSubscribe(Subsystem.withName(subsystem), s"$componentVal*")
      case _ =>
        subscriber.pSubscribe(Subsystem.withName(subsystem), "*")
    }
  }
}
