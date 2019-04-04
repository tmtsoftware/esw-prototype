package ocs

import ocs.framework.ScriptImports._

class OcsDarkNight(csw: CswServices) extends Script(csw) {

  private val iris = csw.sequencerCommandService("iris")
  private val tcs  = csw.sequencerCommandService("tcs")

  private var eventCount   = 0
  private var commandCount = 0

  private val publisherStream = csw.publish(10.seconds) {
    Option(SystemEvent(Prefix("ocs-test"), EventName("system")))
  }
  private val subscriptionStream = csw.subscribe(Set(EventKey("ocs-test.system"))) { eventKey =>
    eventCount = eventCount + 1
    println(s"------------------> received-event for ocs on key: $eventKey")
    Done
  }

  handleSetupCommand("setup-iris") { commandA =>
    spawn {
      println(s"[Ocs] Received command: ${commandA.commandName}")
      csw.sendResult(s"[Ocs] Received command: ${commandA.commandName}")
      var topLevelCommands: Set[SequenceCommand] = Set(commandA)

      val maybeCommandB = nextIf(c => c.commandName.name == "setup-iris").await
      val sequence = if (maybeCommandB.isDefined) {
        val commandB = maybeCommandB.get
        topLevelCommands += commandB

        val commandB1 = Setup(Prefix("test-commandB1"), CommandName("setup-iris"), Some(ObsId("test-obsId")))

        Sequence(commandA, commandB, commandB1)
      } else Sequence.empty

      val response = iris.await.submit(sequence).await

      csw.addSequenceResponse(topLevelCommands, response)
      Done
    }
  }

  handleSetupCommand("setup-iris-tcs") { commandC =>
    spawn {
      val maybeCommandD = nextIf(c2 => c2.commandName.name == "setup-iris-tcs").await
      val tcsSequence = if (maybeCommandD.isDefined) {
        val commandD = maybeCommandD.get
        Sequence(commandD)
      } else {
        Sequence.empty
      }

      println(s"[Ocs] Received command: ${commandC.commandName}")
      val irisSequence = Sequence(commandC)

      val responses = par(
        iris.await.submit(irisSequence),
        tcs.await.submit(tcsSequence)
      ).await

      csw.addSequenceResponse(Set(commandC), responses.head)

      if (maybeCommandD.isDefined) {
        csw.addSequenceResponse(Set(maybeCommandD.get), responses.last)
      }

      println(s"[Ocs] Received response: $responses")
      csw.sendResult(s"$responses")

      Done
    }
  }

  handleSetupCommand("setup-tcs") { command =>
    spawn {
      println(s"[Ocs] Received command: ${command.commandName}")

      val response = tcs.await.submit(Sequence(command)).await

      csw.addSequenceResponse(Set(command), response)
      csw.sendResult(s"$response")
      Done
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    println("shutdown ocs")
    subscriptionStream.unsubscribe().await
    publisherStream.cancel()
    Done
  }
}
