package scripts.iris

import tmt.sequencer.ScriptImports._

class IrisDarkNight(cs: CswServices) extends Script(cs) {

  cs.handleCommand("setup-iris") { command =>
    spawn {
      cs.log(s"Command ${command.commandName} received by ${cs.sequencerId}")
      var firstAssemblyResponse: CommandResponse = null
      var counter                                = 0
      loop {
        spawn {
          counter += 1
          cs.log(s"Command ${command.commandName} sending to Sample1Assembly")
          firstAssemblyResponse = cs.setup("Sample1Assembly", command).await
          println(counter)
          stopWhen(counter > 2)
        }
      }.await
      println(s"[Iris] Received command: ${command.commandName}")
      val response = AggregateResponse
        .add(firstAssemblyResponse)
        .markSuccessful(command)

      cs.log(s"[Iris] Received response: $response")
      println(s"[Iris] Received response: $response")
      response
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    println("shutdown iris")
    Done
  }
}
