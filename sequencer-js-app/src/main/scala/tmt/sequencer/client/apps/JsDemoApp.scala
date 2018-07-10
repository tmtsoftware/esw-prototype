package tmt.sequencer.client.apps

import play.api.libs.json.Json
import tmt.sequencer.SequenceFeederClient
import tmt.sequencer.models.{CommandListWeb, SequenceCommandWeb, WebRWSupport}
import ujson.Js

object JsDemoApp extends WebRWSupport {
  def main(args: Array[String]): Unit = {

    import scala.concurrent.ExecutionContext.Implicits.global

    val client = new SequenceFeederClient("http://0.0.0.0:8000")

    val commandListWeb = CommandListWeb(
      Seq(
        SequenceCommandWeb(
          "Setup",
          "53ba55f9-26f5-4998-9bfe-b320b3f92d64",
          "test1",
          "setup-iris",
          Some("test-obsId1"),
          Js.Arr()
        )
      )
    )

    println(upickle.default.write(commandListWeb))

    val res = client.feed(commandListWeb)
    res.onComplete(println)
  }
}
