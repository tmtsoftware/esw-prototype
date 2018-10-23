package iris

import ocs.framework.ScriptImports._
import ocs.framework.dsl

class IrisDarkNight(csw: CswServices) extends dsl.Script(csw) {

  var flag = true

  handleSetupCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.commandName}")

      val command1 = Setup(Prefix("test-command1"), CommandName("command1"), Some(ObsId("test-obsId")))
      val command2 = Setup(Prefix("test-command2"), CommandName("command2"), Some(ObsId("test-obsId")))

      csw.addSubCommand(command.runId, command1.runId)
      csw.addSubCommand(command.runId, command2.runId)

      val assemblyResponse1 = csw.submit("Sample1Assembly", command1).await
      val assemblyResponse2 = csw.submit("Sample1Assembly", command2).await

      csw.updateSubCommand(command1.runId, assemblyResponse1)
      csw.updateSubCommand(command2.runId, assemblyResponse2)

      Done
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    println("shutdown iris")
    Done
  }

  override def abort(): Future[Done] = spawn {
    flag = true
    Done
  }
}
