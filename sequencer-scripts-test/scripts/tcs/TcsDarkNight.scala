package tcs

import ocs.framework.ScriptImports._

class TcsDarkNight(csw: CswServices) extends Script(csw) {

  private var eventCount   = 0
  private var commandCount = 0

  handleSetupCommand("setup-tcs") { command =>
    spawn {
      println(s"[Tcs] Received command: ${command.commandName}")

      val firstAssemblyResponse = csw.submit("Sample1Assembly", command).await
      val commandFailed         = firstAssemblyResponse.isInstanceOf[CommandResponse.Error]

      if (commandFailed) {
        val command2 = Setup(Prefix("test-command2"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))
        csw.submit("Sample1Assembly", command2).await
      } else {
        val command3 = Setup(Prefix("test-command3"), CommandName("setup-tcs"), Some(ObsId("test-obsId")))
        csw.submit("Sample1Assembly", command3).await
      }

      csw.addOrUpdateCommand(firstAssemblyResponse)

      println(s"[Tcs] Received response: $firstAssemblyResponse")
      csw.sendResult(s"$firstAssemblyResponse")
      Done
    }
  }
}
