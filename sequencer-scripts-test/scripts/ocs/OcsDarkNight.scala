package ocs

import ocs.framework.ScriptImports._
import ocs.framework.dsl

class OcsDarkNight(csw: CswServices) extends dsl.Script(csw) {

  private val iris = csw.sequencerCommandService("iris")
  private val tcs  = csw.sequencerCommandService("tcs")

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
      val maybeCommandB = nextIf(c => c.commandName.name == "setup-iris").await
      val subCommandsB = if (maybeCommandB.isDefined) {
        val commandB  = maybeCommandB.get
        val commandB1 = Setup(Prefix("test-commandB1"), CommandName("setup-iris"), Some(ObsId("test-obsId")))
        Sequence.from(commandB, commandB1)
      } else Sequence.empty

      val commandList = subCommandsB.add(commandA)

      iris.await.submit(commandList).await

      val commandAResponse = Completed(commandA.runId)

      val response = if (maybeCommandB.isDefined) {
        AggregateResponse(commandAResponse, Completed(maybeCommandB.get.runId))
      } else {
        AggregateResponse(commandAResponse)
      }

      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response

      Done
    }
  }

  handleSetupCommand("setup-iris-tcs") { commandC =>
    spawn {
      val maybeCommandD = nextIf(c2 => c2.commandName.name == "setup-iris-tcs").await
      val tcsSequence = if (maybeCommandD.isDefined) {
        val commandD = maybeCommandD.get
        Sequence.from(commandD)
      } else {
        Sequence.empty
      }

      println(s"[Ocs] Received command: ${commandC.commandName}")
      val irisSequence = Sequence.from(commandC)

      parAggregate(
        iris.await.submit(irisSequence),
        tcs.await.submit(tcsSequence)
      ).await

      val commandCResponse = Completed(commandC.runId)

      val response = if (maybeCommandD.isDefined) {
        AggregateResponse(commandCResponse, Completed(maybeCommandD.get.runId))
      } else {
        AggregateResponse(commandCResponse)
      }

      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      response

      Done
    }
  }

  handleSetupCommand("setup-tcs") { command =>
    spawn {
      println(s"[Ocs] Received command: ${command.commandName}")

      tcs.await.submit(Sequence.from(command)).await

      val response = AggregateResponse(Completed(command.runId))
      println(s"[Ocs] Received response: $response")
      csw.sendResult(s"$response")
      Done
    }
  }

  override def onShutdown(): Future[Done] = spawn {
    subscriptionStream.unsubscribe().await
    publisherStream.cancel()
    println("shutdown ocs")
    Done
  }
}
