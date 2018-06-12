import tmt.sequencer.ScriptImports._

class TcsDarkNight(cs: CswServices) extends Script(cs) {

  var eventCount = 0
  var commandCount = 0

  cs.handleCommand("setup-tcs") { command =>
    spawn {
      println(s"[Tcs] Received command: ${command.commandName}")

      val firstAssemblyResponse = cs.setup("tcs-assembly1", command.withId(Id(s"${command.runId}a"))).await
      val commandFailed = firstAssemblyResponse.isInstanceOf[CommandResponse.Failed]

      val restAssemblyResponses = if (commandFailed) {
        Set(cs.setup("tcs-assembly2", command.withId(Id(s"${command.runId}c"))).await)
      } else {
        Set(cs.setup("tcs-assembly3", command.withId(Id(s"${command.runId}b"))).await)
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
