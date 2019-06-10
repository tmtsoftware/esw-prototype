package iris

import akka.Done
import akka.actor.ActorSystem
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.Prefix
import ocs.framework.ScriptImports.{CswServices, Script}
import ocs.framework.core.SequenceOperator
import ocs.framework.dsl.{ControlDsl, ScriptDsl}
import ocs.testkit.mocks.CswServicesMock
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar.mock

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

trait HandleTest1 extends ScriptDsl {
  println(s"[${Thread.currentThread().getName}] HandleTest1 Constructor")

  handleSetupCommand("test1") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}]  Received $cmd")
      Done
    }
  }

  handleShutdown {
    spawn {
      println(s"[${Thread.currentThread().getName}] HandleTest1 onShutdown")
      Done
    }
  }
}

trait HandleTestDsl extends ScriptDsl {
  println(s"[${Thread.currentThread().getName}] HandleTest2 Constructor")

  handleSetupCommand("test2") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}]  Received $cmd")

      Done
    }
  }

  handleShutdown {
    spawn {
      println(s"[${Thread.currentThread().getName}] HandleTest2 onShutdown")
      Done
    }
  }
}

class HandleTest3(cswServices: CswServices) extends Script(cswServices) {

  println(s"[${Thread.currentThread().getName}] HandleTest3 Constructor")

  handleSetupCommand("test1") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] HandleTest3 Received $cmd")
      Done
    }
  }

}

class HandleTest4(cswServices: CswServices) extends Script(cswServices) {
  println(s"[${Thread.currentThread().getName}] HandleTest4 Constructor")

  handleSetupCommand("test1") { cmd ⇒
    spawn {
      println(s"[${Thread.currentThread().getName}] HandleTest4 Received $cmd")
      Done
    }
  }
}

class TestScript(csw: CswServices) extends Script(csw) with HandleTest1 with HandleTestDsl {
  println(s"[${Thread.currentThread().getName}] TestScript Constructor")

}

class TestScript1(csw: CswServices) extends Script(csw) {
  println(s"[${Thread.currentThread().getName}] TestScript Constructor")

  injectHandlers(
    new HandleTest3(csw),
    new HandleTest4(csw)
  )
}

class ScriptTest extends FunSuite {

  private implicit val system: ActorSystem = ActorSystem("test")
  private val mockSequenceOperator         = mock[SequenceOperator]
  private val mockCswServices              = CswServicesMock.create(mockSequenceOperator)

  test("testing script mixin") {
    val testScript = new TestScript(mockCswServices)

    val eventualResponse = testScript.execute(Setup(Prefix("sequencer"), CommandName("test1"), None))
    println(Await.result(eventualResponse, 10.seconds))

    val eventualResponse1 = testScript.execute(Setup(Prefix("sequencer"), CommandName("test2"), None))
    println(Await.result(eventualResponse1, 10.seconds))

    Await.result(testScript.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }

}