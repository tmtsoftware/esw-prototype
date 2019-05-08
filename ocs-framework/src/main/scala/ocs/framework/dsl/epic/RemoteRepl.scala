package ocs.framework.dsl.epic

import ammonite.sshd._
import ocs.framework.CswSystem
import org.apache.sshd.server.auth.password.AcceptAllPasswordAuthenticator

// find a workaround for server not hanging after multiple connects/disconnects
// explore timeout thing
class RemoteRepl(
    cswSystem: CswSystem,
) {
  def server() = new SshdRepl(
    SshServerConfig(
      address = "0.0.0.0",
      port = 22222,
      passwordAuthenticator = Some(AcceptAllPasswordAuthenticator.INSTANCE) // or publicKeyAuthenticator
    ),
    predef = """
         |import scala.concurrent.duration.Duration
         |import scala.concurrent.{Await, Future}
         |import ocs.framework.dsl.epic.internal.event.MockEvent
         |implicit class RichFuture[T](val f: Future[T]) {
         |  def get: T = Await.result(f, Duration.Inf)
         |}
      """.stripMargin,
    replArgs = Seq(
      "csw" -> cswSystem,
      "es" -> cswSystem.mockEventService,
    )
  )
}
