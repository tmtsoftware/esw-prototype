package scripts.iris

import tmt.sequencer.ScriptImports._

class IrisDarkNight(cs: CswServices) extends Script(cs) {

  cs.handleCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.commandName}")
      var firstAssemblyResponse: CommandResponse = null
      var counter                                = 0
      loop {
        spawn {
          counter += 1
          firstAssemblyResponse = cs.setup("Sample1Assembly", command).await
          println(counter)
          stopWhen(counter > 2)
        }
      }.await

      val response = AggregateResponse
        .add(firstAssemblyResponse)
        .markSuccessful(command)

      println(s"[Iris] Received response: $response")
      cs.sendResult(s"$response")
      response
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    println("shutdown iris")
    Done
  }
}
