package ocs.framework.util

import csw.params.core.generics.{Key, KeyType}
import csw.params.events.SystemEvent
import ocs.framework.ScriptImports.{EventName, Prefix}

object ResultEvent {
  val resultMsgKey: Key[String] = KeyType.StringKey.make("sequencer-result")
  val eventName                 = EventName("result")

  def createResultEvent(sequencerId: String, msg: String): SystemEvent = {
    val resultMsgParam = resultMsgKey.set(msg)
    SystemEvent(Prefix(sequencerId), eventName, Set(resultMsgParam))
  }
}
