package ocs.api.client

import csw.params.events.Event
import ocs.api.codecs.AssemblyJsonSupport
import ocs.api.{EventStream, WebGateway}

class EventJsClient(gateway: WebGateway) extends AssemblyJsonSupport {
  def subscribe(subsystem: String, component: Option[String] = None, event: Option[String] = None): EventStream[Event] = {
    val componentAttr = component.map(c => s"component=$c")
    val eventAttr     = event.map(e => s"event=$e")
    val attrs         = (componentAttr ++ eventAttr).mkString("&")
    val attrStr       = if (attrs.isEmpty) "" else s"?$attrs"
    gateway.stream[Event](s"/events/subscribe/$subsystem$attrStr")
  }
}
