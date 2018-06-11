package tmt.sequencer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import tmt.sequencer.FutureExt.RichFuture
import tmt.sequencer.dsl.ActiveObject

import scala.concurrent.Future

class ActiveObjectDemo extends FunSuite with BeforeAndAfterAll with ActiveObject {

  private implicit val actorSystem: ActorSystem        = ActorSystem("test")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  override protected def afterAll(): Unit = {}

  test("future") {
    var x = 0

    println(x)

    def incr(): Unit = x += 1
    def decr(): Unit = x -= 1

    Future
      .sequence(
        List(
          Source(1 to 10000).mapAsync(1)(d => spawn(incr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(decr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(incr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(decr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(incr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(decr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(incr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(decr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(incr())).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(d => spawn(decr())).runForeach(_ => ()),
        )
      )
      .get

    println(x)

  }

  test("future-atomic") {

    var x = 0

    var wasIncr = false

    println(x)

    def f(d: Int) = spawn {
      if (wasIncr) {
        x = x - d
        wasIncr = false
      } else {
        x = x + d
        wasIncr = true
      }
    }

    Future
      .sequence(
        List(
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
          Source(1 to 10000).mapAsync(1)(_ => f(1)).runForeach(_ => ()),
        )
      )
      .get

    println(x)

  }

  test("custom-async-await") {

    val f1 = spawn(10)
    val f2 = spawn(20)

    spawn {
      f1.await + f2.await
    }.map(println).get
  }

}
