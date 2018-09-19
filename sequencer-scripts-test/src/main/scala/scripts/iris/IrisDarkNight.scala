package scripts.iris

import tmt.ocs.ScriptImports._

class IrisDarkNight(cs: CswServices) extends Script(cs) {

  val publisherStream = cs.publish(10.seconds) {
    SystemEvent(Prefix("iris-test"), EventName("system"))
  }

  val subscriptionStream = cs.subscribe(Set(EventKey("iris-test.system"))) { _ =>
    println(s"------------------> received-event for ocs on key-------------------->")
    Done
  }

  loop(minimumInterval = 2.second) {
    spawn {
      println("************ loop **************")
      stopWhen(false)
    }
  }

  cs.handleCommand("setup-iris") { command =>
    spawn {
      println(s"[Iris] Received command: ${command.commandName}")
      var firstAssemblyResponse: CommandResponse = null
      var counter                                = 0
      loop {
        spawn {
          counter += 1
          firstAssemblyResponse = cs.setup("Sample1Assembly", command).await
          println(counter)
          stopWhen(counter > 2)
        }
      }.await

      val response = AggregateResponse(firstAssemblyResponse)
        .markSuccessful(command)

      println(s"[Iris] Received response: $response")
      cs.sendResult(s"$response")
      response
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    println("shutdown iris")
    Done
  }
}
