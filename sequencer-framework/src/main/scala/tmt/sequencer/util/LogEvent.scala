package tmt.sequencer.util

import csw.messages.events.SystemEvent
import csw.messages.params.generics.{Key, KeyType}
import tmt.sequencer.ScriptImports.{EventName, Prefix}

object LogEvent {
  val logMsgKey: Key[String] = KeyType.StringKey.make("sequencer-log")
  val eventName = EventName("log")

  def createLogEvent(sequencerId: String, msg: String): SystemEvent = {
    val logMsgParam = logMsgKey.set(msg)
    SystemEvent(Prefix(sequencerId), eventName, Set(logMsgParam))
  }
}
