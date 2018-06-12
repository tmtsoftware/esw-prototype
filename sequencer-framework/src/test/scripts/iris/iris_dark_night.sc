import tmt.sequencer.ScriptImports._

class IrisDarkNight(cs: CswServices) extends Script(cs) {

  var eventCount = 0
  var commandCount = 0

  cs.handleCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.name}")

      val firstAssemblyResponse = cs.setup("SampleAssembly", command.withId(Id(s"${command.id}a"))).await
      val response = AggregateResponse
        .add(firstAssemblyResponse)
        .markSuccessful(command)

      println(s"[Iris] Received response: $response")
      response
    }
  }
}
