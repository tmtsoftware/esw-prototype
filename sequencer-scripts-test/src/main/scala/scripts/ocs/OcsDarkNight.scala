package scripts.ocs

import tmt.ocs.ScriptImports._

class OcsDarkNight(csw: CswServices) extends Script(csw) {

  val iris = csw.sequenceFeeder("iris")
  val tcs  = csw.sequenceFeeder("tcs")

  var eventCount   = 0
  var commandCount = 0

  val publisherStream = csw.publish(10.seconds) {
    SystemEvent(Prefix("ocs-test"), EventName("system"))
  }

  val subscriptionStream = csw.subscribe(Set(EventKey("ocs-test.system"))) { eventKey =>
    eventCount = eventCount + 1
    println(s"------------------> received-event for ocs on key: $eventKey")
    Done
  }

  handleCommand("setup-iris") { commandA =>
    spawn {
      println(s"[Ocs] Received command: ${commandA.commandName}")
      csw.sendResult(s"[Ocs] Received command: ${commandA.commandName}")
      val maybeCommandB = nextIf(c => c.commandName.name == "setup-iris").await
      val subCommandsB = if (maybeCommandB.isDefined) {
        val commandB  = maybeCommandB.get
        val commandB1 = Setup(Prefix("test-commandB1"), CommandName("setup-iris"), Some(ObsId("test-obsId")))
        CommandList.from(commandB, commandB1)
      } else CommandList.empty

      val commandList = subCommandsB.add(commandA)

      val response = iris.submit(commandList).await.markSuccessful(commandA).markSuccessful(maybeCommandB)
      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response
    }
  }

  handleCommand("setup-iris-tcs") { commandC =>
    spawn {
      val maybeCommandD = nextIf(c2 => c2.commandName.name == "setup-iris-tcs").await
      val tcsSequence = if (maybeCommandD.isDefined) {
        val nextCommand = maybeCommandD.get
        CommandList.from(nextCommand)
      } else {
        CommandList.empty
      }

      println(s"[Ocs] Received command: ${commandC.commandName}")
      val irisSequence = CommandList.from(commandC)

      val aggregateResponse = parAggregate(
        iris.submit(irisSequence),
        tcs.submit(tcsSequence)
      ).await

      val response = aggregateResponse.markSuccessful(commandC).markSuccessful(maybeCommandD)

      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response
    }
  }

  handleCommand("setup-tcs") { command =>
    spawn {
      println(s"[Ocs] Received command: ${command.commandName}")

      val responseE = tcs.submit(CommandList.from(command)).await.markSuccessful(command)

      println(s"[Ocs] Received response: $responseE")
      csw.sendResult(s"$responseE")
      responseE
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    subscriptionStream.unsubscribe().await
    publisherStream.cancel()
    println("shutdown ocs")
    Done
  }
}
