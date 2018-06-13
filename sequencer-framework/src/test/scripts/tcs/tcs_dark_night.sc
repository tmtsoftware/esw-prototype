import tmt.sequencer.ScriptImports._

class TcsDarkNight(cs: CswServices) extends Script(cs) {

  var eventCount = 0
  var commandCount = 0

  cs.handleCommand("setup-tcs") { command =>
    spawn {
      println(s"[Tcs] Received command: ${command.commandName}")

      val firstAssemblyResponse = cs.setup("SampleAssembly", command).await
      val commandFailed = firstAssemblyResponse.isInstanceOf[CommandResponse.Failed]

      val restAssemblyResponses = if (commandFailed) {
        val command2 = Setup(Prefix("test-command2"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))
        Set(cs.setup("SampleAssembly", command2).await)
      } else {
        val command3 = Setup(Prefix("test-command3"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))
        Set(cs.setup("SampleAssembly", command3).await)
      }


      val response = AggregateResponse
        .add(firstAssemblyResponse)
        .add(restAssemblyResponses)
        .markSuccessful(command)

      println(s"[Tcs] Received response: $response")
      response
    }
  }
}
