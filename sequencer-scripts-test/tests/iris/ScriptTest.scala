package iris

import akka.Done
import akka.actor.ActorSystem
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.Prefix
import ocs.framework.ScriptImports.{CswServices, Script}
import ocs.framework.core.SequenceOperator
import ocs.framework.dsl.ScriptDsl
import ocs.testkit.mocks.CswServicesMock
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar.mock

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

trait WFOSSExposureHandler extends ScriptDsl {
  println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] Constructor")

  handleSetupCommand("wfos-exposure") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] Received $cmd")
      Done
    }
  }

  handleShutdown {
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSSExposureHandler] onShutdown")
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

  handleShutdown {
    spawn {
      println(s"[${Thread.currentThread().getName}] [WFOSObserveHandler] onShutdown")
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

class WFOSScript(csw: CswServices) extends Script(csw) with WFOSObserveHandler with WFOSSExposureHandler  {
  println(s"[${Thread.currentThread().getName}] [WFOSScript] Constructor")

}

class OcsScript(csw: CswServices) extends Script(csw) {
  println(s"[${Thread.currentThread().getName}] [OcsScript] Constructor")

  private val irisHandlers = new IrisHandlers(csw)
  private val tcsHandlers = new TcsHandlers(csw)

  handleSetupCommand("ocs") { cmd ⇒
    spawn {

      val irisSetup = cmd.copy(commandName = CommandName("iris"))
      val tcsSetup = cmd.copy(commandName = CommandName("tcs"))

      par(
        irisHandlers.execute(irisSetup),
        tcsHandlers.execute(tcsSetup)
      ).await
    }

  }

}

class ScriptTest extends FunSuite {

  private implicit val system: ActorSystem = ActorSystem("test")
  private val mockSequenceOperator         = mock[SequenceOperator]
  private val mockCswServices              = CswServicesMock.create(mockSequenceOperator)

  test("mixin approach") {
    val wFOSScript = new WFOSScript(mockCswServices)

    Await.result(wFOSScript.execute(Setup(Prefix("sequencer"), CommandName("wfos-observe"), None)),10.seconds)
    Await.result(wFOSScript.execute(Setup(Prefix("sequencer"), CommandName("wfos-exposure"), None)), 10.seconds)

    Await.result(wFOSScript.abort(), 10.seconds)
    Await.result(wFOSScript.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }

  test("new scripts approach") {
    val ocsScript = new OcsScript(mockCswServices)

    val eventualResponse = ocsScript.execute(Setup(Prefix("sequencer"), CommandName("ocs"), None))

    println(Await.result(eventualResponse, 10.seconds))

    Await.result(ocsScript.abort(), 10.seconds)
    Await.result(ocsScript.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }
}
