package ocs.framework

import ammonite.util.Res
import csw.logging.scaladsl.LoggingSystemFactory

object TestAmmoniteApp {

  def main(args: Array[String]): Unit = {
    val wiring = new TestAmmoniteWiring()
    import wiring._
    LoggingSystemFactory.start("sample", "", "", system)

    val (result: Res[Any], paths) = ammonite
      .Main(
        predefCode = """
                |import scala.concurrent.duration.Duration
                |import scala.concurrent.{Await, Future}
                |import csw.params.core.generics.KeyType._
                |import csw.params.commands._
                |import csw.params.core.models._
                |import ocs.api.messages.SequencerMsg._
                |import ocs.api.messages.SupervisorMsg._
                |import ocs.api.models.Sequence
                |implicit class RichFuture[T](val f: Future[T]) {
                |  def get: T = Await.result(f, Duration.Inf)
                |}
                | """.stripMargin
      )
      .run(
        "componentFactory" -> componentFactory
      )

    println(paths)
    println(result.asInstanceOf[Res.Exception].t.printStackTrace())
  }
}
