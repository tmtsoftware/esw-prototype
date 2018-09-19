package scripts.iris

import tmt.ocs.ScriptImports._
import tmt.ocs.dsl.CommandDsl

class IrisDarkNight(csw: CswServices, cs: CommandDsl) extends Script(csw, cs) {

  val publisherStream = csw.publish(10.seconds) {
    SystemEvent(Prefix("iris-test"), EventName("system"))
  }

  val subscriptionStream = csw.subscribe(Set(EventKey("iris-test.system"))) { _ =>
    println(s"------------------> received-event for ocs on key-------------------->")
    Done
  }

  loop(minimumInterval = 2.second) {
    spawn {
      println("************ loop **************")
      stopWhen(false)
    }
  }

  cs.handleSetupCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.commandName}")
      var firstAssemblyResponse: CommandResponse = null
      var counter                                = 0
      loop {
        spawn {
          counter += 1
          firstAssemblyResponse = csw.setup("Sample1Assembly", command).await
          println(counter)
          stopWhen(counter > 2)
        }
      }.await

      val response = AggregateResponse(firstAssemblyResponse)
        .markSuccessful(command)

      println(s"[Iris] Received response: $response")
      csw.sendResult(s"$response")
      response
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    println("shutdown iris")
    Done
  }
}
