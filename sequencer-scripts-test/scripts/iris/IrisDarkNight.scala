package iris

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
      var firstAssemblyResponse: CommandResponse = null
      loop {
        spawn {
          firstAssemblyResponse = csw.submit("Sample1Assembly", command).await
          stopWhen(flag)
        }
      }.await

      val response = AggregateResponse(CommandResponse.Completed(command.runId))

      println(s"[Iris] Received response: $response")
      csw.sendResult(s"$response")
      response
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
