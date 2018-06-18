import tmt.sequencer.ScriptImports._

class OcsDarkNight(cs: CswServices) extends Script(cs) {

  val iris = cs.sequenceProcessor("iris")
  val tcs = cs.sequenceProcessor("tcs")

  var eventCount = 0
  var commandCount = 0

//  val cancellable = cs.publish(10.seconds) {
//    SystemEvent(Prefix("ocs-test"), EventName("system"))
//  }
//
//  val subscription = cs.subscribe(Set(EventKey("ocs-test.system"))) { eventKey =>
//    eventCount = eventCount + 1
//    println(s"------------------> received-event on key: $eventKey")
//    Done
//  }

  cs.handleCommand("setup-iris") { commandA =>
    spawn {
      val maybeCommandB = cs.nextIf(c => c.commandName == "setup-iris").await
      val subCommandsB = if (maybeCommandB.isDefined) {
        val commandB  = maybeCommandB.get
        val commandB1 = Setup(Prefix("test-commandB1"), CommandName("setup-iris"), Some(ObsId("test-obsId")))
        CommandList.from(commandB, commandB1)
      } else CommandList.empty

      println(s"[Ocs] Received commandA: ${commandA.commandName}")

      val commandList = subCommandsB.add(commandA)

      val response = iris.feed(commandList).await.markSuccessful(commandA).markSuccessful(maybeCommandB)

      println(s"[Ocs] Received response: $response")
      response
    }
  }

  cs.handleCommand("setup-iris-tcs") { commandC =>
    spawn {
      val maybeCommandD = cs.nextIf(c2 => c2.commandName == "setup-iris-tcs").await
      val tcsSequence = if (maybeCommandD.isDefined) {
        val nextCommand = maybeCommandD.get
        CommandList.from(nextCommand)
      } else {
        CommandList.empty
      }

      println(s"[Ocs] Received commandC: ${commandC.commandName}")
      val irisSequence = CommandList.from(commandC)

      val aggregateResponse = parAggregate(
        iris.feed(irisSequence),
        tcs.feed(tcsSequence)
      ).await

      val response = aggregateResponse.markSuccessful(commandC).markSuccessful(maybeCommandD)

      println(s"[Ocs] Received response: $response")
      response
    }
  }

  cs.handleCommand("setup-tcs") { command =>
    spawn {
      println(s"[Ocs] Received command: ${command.commandName}")

      val responseE = tcs.feed(CommandList.from(command)).await.markSuccessful(command)

      println(s"[Ocs] Received response: $responseE")
      responseE
    }
  }

  override def onShutdown(): Future[Done] = spawn {
//    subscription.unsubscribe()
//    cancellable.cancel()
    println("shutdown ocs")
    Done
  }
}
