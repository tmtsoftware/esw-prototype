package tmt.sequencer.client.apps

import tmt.sequencer.SequenceFeederClient
import tmt.sequencer.models.{CommandListWeb, SequenceCommandWeb}
import ujson.Js

object JsDemoApp {
  def main(args: Array[String]): Unit = {
    import monix.execution.Scheduler.Implicits.global

    val client = new SequenceFeederClient("http://0.0.0.0:8000")

    val commandListWeb = CommandListWeb(
      Seq(
        SequenceCommandWeb("Setup", "53ba55f9-26f5-4998-9bfe-b320b3f92d64", "test1", "setup-iris", Some("test-obsId1"), Js.Arr())
      )
    )

    val str = upickle.default.write[CommandListWeb](commandListWeb)

    println(str)
    println(upickle.default.read[CommandListWeb](str))

    val res = client.feed(commandListWeb)
    res.onComplete(println)
  }
}
