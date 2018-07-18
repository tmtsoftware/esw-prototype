package tmt.sequencer.util

import csw.messages.events.SystemEvent
import csw.messages.params.generics.{Key, KeyType}
import tmt.sequencer.ScriptImports.{EventName, Prefix}

object ResultEvent {
  val resultMsgKey: Key[String] = KeyType.StringKey.make("sequencer-result")
  val eventName                 = EventName("result")

  def createResultEvent(sequencerId: String, msg: String): SystemEvent = {
    val resultMsgParam = resultMsgKey.set(msg)
    SystemEvent(Prefix(sequencerId), eventName, Set(resultMsgParam))
  }
}
