package iris

import csw.params.commands.CommandResponse.SubmitResponse
import ocs.framework.ScriptImports._
import ocs.framework.dsl

class IrisDarkNight(csw: CswServices) extends dsl.Script(csw) {

  var flag = true

  private val publisherStream = csw.publish(10.seconds) {
    SystemEvent(Prefix("iris-test"), EventName("system"))
  }

  private val subscriptionStream = csw.subscribe(Set(EventKey("iris-test.system"))) { _ =>
    println(s"------------------> received-event for ocs on key-------------------->")
    Done
  }

  loop(minimumInterval = 2.second) {
    spawn {
      println("************ loop **************")
      stopWhen(false)
    }
  }

  handleSetupCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.commandName}")
      var firstAssemblyResponse: SubmitResponse = null
      loop {
        spawn {
          firstAssemblyResponse = csw.submit("Sample1Assembly", command).await
          stopWhen(flag)
        }
      }.await

      csw.addOrUpdateCommand(command.runId, firstAssemblyResponse)
      Done
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    subscriptionStream.unsubscribe().await
    publisherStream.cancel()
    println("shutdown iris")
    Done
  }

  override def onStart(): Future[Done] = spawn {
    flag = false
    Done
  }

  override def onStop(): Future[Done] = spawn {
    flag = true
    Done
  }
}
