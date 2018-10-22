package ocs

import csw.params.commands.CommandResponse.Completed
import ocs.framework.ScriptImports._
import ocs.framework.dsl

class OcsDarkNight(csw: CswServices) extends dsl.Script(csw) {

  private val iris = csw.sequenceFeeder("iris")
  private val tcs  = csw.sequenceFeeder("tcs")

  private var eventCount   = 0
  private var commandCount = 0

  private val publisherStream = csw.publish(10.seconds) {
    SystemEvent(Prefix("ocs-test"), EventName("system"))
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
      val maybeCommandB             = nextIf(c => c.commandName.name == "setup-iris").await
      var commandB: SequenceCommand = null
      val subCommandsB = if (maybeCommandB.isDefined) {
        commandB = maybeCommandB.get
        val commandB1 = Setup(Prefix("test-commandB1"), CommandName("setup-iris"), Some(ObsId("test-obsId")))
        CommandList.from(commandB, commandB1)
      } else CommandList.empty

      val commandList = subCommandsB.add(commandA)

      iris.await.submit(commandList).await

      val response = AggregateResponse(Completed(commandA.runId), Completed(commandB.runId))

      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response
    }
  }

  handleSetupCommand("setup-iris-tcs") { commandC =>
    spawn {
      val maybeCommandD             = nextIf(c2 => c2.commandName.name == "setup-iris-tcs").await
      var commandD: SequenceCommand = null
      val tcsSequence = if (maybeCommandD.isDefined) {
        commandD = maybeCommandD.get
        CommandList.from(commandD)
      } else {
        CommandList.empty
      }

      println(s"[Ocs] Received command: ${commandC.commandName}")
      val irisSequence = CommandList.from(commandC)

      parAggregate(
        iris.await.submit(irisSequence),
        tcs.await.submit(tcsSequence)
      ).await

      val response = AggregateResponse(Completed(commandC.runId), Completed(commandD.runId))

      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response
    }
  }

  handleSetupCommand("setup-tcs") { command =>
    spawn {
      println(s"[Ocs] Received command: ${command.commandName}")

      tcs.await.submit(CommandList.from(command)).await

      val response = AggregateResponse(Completed(command.runId))

      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    subscriptionStream.unsubscribe().await
    publisherStream.cancel()
    println("shutdown ocs")
    Done
  }
}
