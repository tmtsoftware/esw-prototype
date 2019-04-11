package ocs.client

import csw.params.commands.CommandResponse.CompletedWithResult
import csw.params.commands._
import csw.params.core.generics.{Key, KeyType}
import csw.params.core.models._
import ocs.api.models.{Sequence, StepList}
import org.scalatest.FunSuite

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class IrisDemoTest extends FunSuite {

  val s1: String = "encoder"
  val s2: String = "filter"

  val k1: Key[Int] = KeyType.IntKey.make(s1)
  val k2: Key[Int] = KeyType.IntKey.make(s2)

  val wiring = new Wiring()

  import wiring._

  implicit class RichFuture[T](val f: Future[T]) {
    def get: T = Await.result(f, Duration.Inf)
  }

  test("Should be able to submit command to assembly and get response") {
    val expectedResult = Result(Prefix("wfos.prog.cloudcover"), Set(k1.set(1), k2.set(2)))

    val sample1Assembly = componentFactory.assemblyCommandService("Sample1Assembly").get
    val actualResult    = sample1Assembly.submit(Setup(Prefix("test1"), CommandName("setup"), Some(ObsId("test-obsId1")))).get

    assert(actualResult.asInstanceOf[CompletedWithResult].result === expectedResult)
  }

  test("Should be able to submit, pause, and resume a sequence to the sequencer") {
    val expectedResult = Result(Prefix("wfos.prog.cloudcover"), Set(k1.set(1), k2.set(2)))

    val irisCommandService = componentFactory.sequenceCommandService("iris", "darknight").get
    val irisSequenceEditor = componentFactory.sequenceEditor("iris", "darknight").get

    val sequence = Sequence(
      Setup(Prefix("test1"), CommandName("setup-iris"), Some(ObsId("test-obsId1"))),
      Setup(Prefix("test2"), CommandName("setup-iris"), Some(ObsId("test-obsId1"))),
      Setup(Prefix("test3"), CommandName("setup-iris"), Some(ObsId("test-obsId1"))),
      Setup(Prefix("test4"), CommandName("setup-iris"), Some(ObsId("test-obsId1"))),
      Setup(Prefix("test5"), CommandName("setup-iris"), Some(ObsId("test-obsId1")))
    )

    val actualResult = irisCommandService.submit(sequence)

    irisSequenceEditor.pause().get

    val currentSequenceState: StepList = irisSequenceEditor.status.get

    assert(currentSequenceState.isPaused)

    irisSequenceEditor.resume().get

    assert(actualResult.get.asInstanceOf[CompletedWithResult].result === expectedResult)
  }
}
