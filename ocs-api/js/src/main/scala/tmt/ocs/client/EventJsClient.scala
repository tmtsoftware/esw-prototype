package tmt.ocs.client

import csw.params.events.Event
import tmt.ocs.codecs.AssemblyJsonSupport
import tmt.ocs.{EventStream, WebGateway}

class EventJsClient(gateway: WebGateway) extends AssemblyJsonSupport {
  def subscribeToEvents(subsystem: String, component: Option[String] = None, event: Option[String] = None): EventStream[Event] = {
    val componentAttr: Option[String] = component.map(c => s"component=$c")
    val eventAttr: Option[String]     = event.map(e => s"event=$e")
    val attrs: String                 = (componentAttr ++ eventAttr).mkString("&")
    val attrStr                       = if (attrs.isEmpty) "" else s"?$attrs"
    val url                           = s"/events/subscribe/$subsystem$attrStr"
    gateway.stream[Event](url)
  }
}
