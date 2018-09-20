package scripts.iris
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.Prefix
import org.scalatest.FunSuite
import scripts.mocks.CswServicesMock

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble

class IrisDarkNightTest extends FunSuite {
  test("should be able to execute handleCommand for setup-iris command") {
    val mockCswServices = CswServicesMock.createMock
    val irisDarkNight   = new IrisDarkNight(mockCswServices)

    val eventualResponse = irisDarkNight.execute(Setup(Prefix("sequencer"), CommandName("setup-iris"), None))
    Await.result(eventualResponse, 10.seconds)
  }
}
