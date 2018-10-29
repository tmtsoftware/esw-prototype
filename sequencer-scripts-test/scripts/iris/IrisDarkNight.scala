package iris

import ocs.framework.ScriptImports._
import ocs.framework.dsl

class IrisDarkNight(csw: CswServices) extends dsl.Script(csw) {

  var flag = true

  handleSetupCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.commandName}")

      val command1 = Setup(Prefix("test-commandA1"), CommandName("commandA1"), Some(ObsId("test-obsId")))
      val command2 = Setup(Prefix("test-commandA2"), CommandName("commandA2"), Some(ObsId("test-obsId")))

      csw.addSubCommand(command.runId, command1.runId)
      csw.addSubCommand(command.runId, command2.runId)

//      val maybeCommandB = nextIf(c => c.commandName.name == "setup-iris").await
//      if (maybeCommandB.isDefined) {
//        val commandB  = maybeCommandB.get
//        val commandB1 = Setup(Prefix("test-commandB1"), CommandName("setup-iris"), Some(ObsId("test-obsId")))
//        val commandB2 = Setup(Prefix("test-commandB2"), CommandName("setup-iris"), Some(ObsId("test-obsId")))
//
//        csw.addSubCommand(commandB.runId, commandB1.runId)
//        csw.addSubCommand(commandB.runId, commandB2.runId)
//
//        val assemblyResponse3 = csw.submit("Sample1Assembly", commandB1).await
//        val assemblyResponse4 = csw.submit("Sample1Assembly", commandB2).await
//
//        csw.updateSubCommand(commandB1.runId, assemblyResponse3)
//        csw.updateSubCommand(commandB2.runId, assemblyResponse4)
//      }

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
