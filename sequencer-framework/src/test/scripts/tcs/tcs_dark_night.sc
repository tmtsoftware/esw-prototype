import tmt.sequencer.ScriptImports._

class TcsDarkNight(cs: CswServices) extends Script(cs) {

  var eventCount = 0
  var commandCount = 0

  cs.handleCommand("setup-tcs") { command =>
    spawn {
      println(s"[Tcs] Received command: ${command.commandName}")

      val firstAssemblyResponse = cs.setup("tcs-assembly1", command).await
      val commandFailed = firstAssemblyResponse.isInstanceOf[CommandResponse.Failed]

      val response = AggregateResponse
        .add(firstAssemblyResponse)
        .markSuccessful(command)

      println(s"[Tcs] Received response: $response")
      response
    }
  }
}
