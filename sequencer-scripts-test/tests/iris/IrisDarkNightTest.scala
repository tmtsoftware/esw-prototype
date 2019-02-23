package iris

import akka.actor.ActorSystem
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.Prefix
import ocs.api.models.{Step, StepStatus}
import ocs.framework.core.SequenceOperator
import ocs.testkit.mocks.CswServicesMock
import org.mockito.Mockito.when
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationDouble

class IrisDarkNightTest extends FunSuite {
  test("should be able to execute handleCommand for setup-iris command") {
    implicit val system: ActorSystem = ActorSystem("test")
    val mockSequenceOperator         = mock[SequenceOperator]
    val mockCswServices              = CswServicesMock.create(mockSequenceOperator)
    val irisDarkNight                = new IrisDarkNight(mockCswServices)

    val someStepAsExpectedByScript = Some(
      Step(
        Setup(Prefix("sequencer"), CommandName("setup-iris"), None),
        StepStatus.Pending,
        hasBreakpoint = false
      )
    )

    when(mockSequenceOperator.maybeNext).thenReturn(Future.successful(someStepAsExpectedByScript))
    when(mockSequenceOperator.next).thenReturn(Future.successful(someStepAsExpectedByScript.get))

    val eventualResponse = irisDarkNight.execute(Setup(Prefix("sequencer"), CommandName("setup-iris"), None))
    println(Await.result(eventualResponse, 10.seconds))

    Await.result(irisDarkNight.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }
}
