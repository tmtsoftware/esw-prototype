package tcs

import ocs.framework.ScriptImports._
import ocs.framework.dsl

class TcsDarkNight(csw: CswServices) extends dsl.Script(csw) {

  private var eventCount   = 0
  private var commandCount = 0

  handleSetupCommand("setup-tcs") { command =>
    spawn {
      println(s"[Tcs] Received command: ${command.commandName}")

      val firstAssemblyResponse = csw.submit("Sample1Assembly", command).await
      val commandFailed         = firstAssemblyResponse.isInstanceOf[CommandResponse.Error]

      if (commandFailed) {
        val command2 = Setup(Prefix("test-command2"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))
        Set(csw.submit("Sample1Assembly", command2).await)
      } else {
        val command3 = Setup(Prefix("test-command3"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))
        Set(csw.submit("Sample1Assembly", command3).await)
      }

//      val response = AggregateResponse(firstAssemblyResponse)
//        .add(restAssemblyResponses)
//        .markSuccessful(command)

      println(s"[Tcs] Received response: $firstAssemblyResponse")
      csw.sendResult(s"$firstAssemblyResponse")
      firstAssemblyResponse
      Done
    }
  }
}
