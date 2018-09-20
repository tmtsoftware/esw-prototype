package scripts.iris

import akka.actor.ActorSystem
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.Prefix
import org.scalatest.FunSuite
import scripts.mocks.{CswServicesMock, SequencerFactory}

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble

class IrisDarkNightTest extends FunSuite {
  test("should be able to execute handleCommand for setup-iris command") {
    implicit val system: ActorSystem = ActorSystem("test")
    val mockCswServices              = CswServicesMock.create(SequencerFactory.create())
    val irisDarkNight                = new IrisDarkNight(mockCswServices)

    val eventualResponse = irisDarkNight.execute(Setup(Prefix("sequencer"), CommandName("setup-iris"), None))
    println(Await.result(eventualResponse, 10.seconds))

    Await.result(irisDarkNight.shutdown(), 10.seconds)
    Await.result(system.terminate(), 10.seconds)
  }
}
