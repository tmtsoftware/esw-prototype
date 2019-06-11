package iris

import akka.Done
import akka.actor.ActorSystem
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.{Id, Prefix}
import ocs.framework.ScriptImports.{CswServices, Script}
import ocs.framework.core.SequenceOperator
import ocs.framework.dsl.ScriptDsl
import ocs.testkit.mocks.CswServicesMock
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar.mock

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

trait BaseTest extends FunSuite {
  protected implicit val system: ActorSystem           = ActorSystem("test")
  protected val mockSequenceOperator: SequenceOperator = mock[SequenceOperator]
  protected val mockCswServices: CswServices           = CswServicesMock.create(mockSequenceOperator)
}

// ================== mix-in =============================
trait WFOSSExposureHandler extends ScriptDsl {
  println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] Constructor")


  handleSetupCommand("wfos-exposure") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] Received $cmd")
      Done
    }
  }

  handleDiagnosticCommand { hint ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] Diag Mode: Hint = $hint")
      Done
    }
  }

  handleShutdown {
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] handleShutdown")
      Done
    }
  }

  handleAbort {
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] handleAbort")
      Done
    }
  }
}

trait WFOSObserveHandler extends ScriptDsl {
  println(s"[${Thread.currentThread().getName}] [WFOSObserveHandler] Constructor")

  handleSetupCommand("wfos-observe") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSObserveHandler] Received $cmd")

      Done
    }
  }

  handleDiagnosticCommand { hint ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSObserveHandler] Diag Mode: Hint = $hint")
      Done
    }
  }

  handleShutdown {
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSObserveHandler] handleShutdown")
      Done
    }
  }

  handleAbort {
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSObserveHandler] handleAbort")
      Done
    }
  }
}

class WFOSScript(csw: CswServices) extends Script(csw) with WFOSObserveHandler with WFOSSExposureHandler {
  println(s"[${Thread.currentThread().getName}] [WFOSScript] Constructor")
}

class ScriptMixInExample extends BaseTest {

  test("mixin approach") {
    val wfosScript = new WFOSScript(mockCswServices)

    Await.result(wfosScript.execute(Setup(Prefix("sequencer"), CommandName("wfos-observe"), None)), 10.seconds)
    Await.result(wfosScript.execute(Setup(Prefix("sequencer"), CommandName("wfos-exposure"), None)), 10.seconds)
    Await.result(wfosScript.executeDiag("demo"), 10.seconds)

    Await.result(wfosScript.abort(), 10.seconds)
    Await.result(wfosScript.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }
}


// ================== new script =========================
class IrisHandlers(cswServices: CswServices) extends Script(cswServices) {

  println(s"[${Thread.currentThread().getName}] [IrisHandlers] Constructor")

  handleSetupCommand("iris") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [IrisHandlers] Received $cmd")
      Done
    }
  }

}

class TcsHandlers(cswServices: CswServices) extends Script(cswServices) {
  println(s"[${Thread.currentThread().getName}] [TcsHandlers] Constructor")

  handleSetupCommand("tcs") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [TcsHandlers] Received $cmd")
      Done
    }
  }
}

class OcsScript(csw: CswServices) extends Script(csw) {
  println(s"[${Thread.currentThread().getName}] [OcsScript] Constructor")

  private val irisHandlers = new IrisHandlers(csw)
  private val tcsHandlers  = new TcsHandlers(csw)

  handleSetupCommand("ocs") { cmd ⇒
    spawn {
      val irisSetup = cmd.copy(commandName = CommandName("iris"), runId = Id())
      val tcsSetup  = cmd.copy(commandName = CommandName("tcs"), runId = Id())

      println(s"[${Thread.currentThread().getName}] [OcsScript] Received $cmd")

      par(
        irisHandlers.execute(irisSetup),
        tcsHandlers.execute(tcsSetup)
      ).await
    }
  }
}

class ThreadPerScriptExample extends BaseTest {
  test("new scripts approach") {
    val ocsScript = new OcsScript(mockCswServices)

    val eventualResponse = ocsScript.execute(Setup(Prefix("sequencer"), CommandName("ocs"), None))

    Await.result(eventualResponse, 10.seconds)

    Await.result(ocsScript.abort(), 10.seconds)
    Await.result(ocsScript.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }
}
