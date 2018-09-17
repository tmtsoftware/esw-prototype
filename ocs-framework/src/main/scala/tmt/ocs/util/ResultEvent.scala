package tmt.ocs.util

import csw.params.core.generics.{Key, KeyType}
import csw.params.events.SystemEvent
import tmt.ocs.ScriptImports.{EventName, Prefix}

object ResultEvent {
  val resultMsgKey: Key[String] = KeyType.StringKey.make("sequencer-result")
  val eventName                 = EventName("result")

  def createResultEvent(sequencerId: String, msg: String): SystemEvent = {
    val resultMsgParam = resultMsgKey.set(msg)
    SystemEvent(Prefix(sequencerId), eventName, Set(resultMsgParam))
  }
}
