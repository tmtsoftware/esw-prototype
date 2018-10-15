package ocs.framework

import akka.actor.typed.ActorRef
import ammonite.sshd._
import ocs.api.{SequenceEditor, SequenceFeeder}
import ocs.api.messages.SupervisorMsg
import ocs.framework.core.SequenceOperator
import ocs.framework.dsl.CswServices
import org.apache.sshd.server.auth.password.AcceptAllPasswordAuthenticator

// find a workaround for server not hanging after multiple connects/disconnects
// explore timeout thing
class RemoteRepl(
    commandService: CswServices,
    sequencer: SequenceOperator,
    supervisor: ActorRef[SupervisorMsg],
    sequenceFeeder: SequenceFeeder,
    sequenceEditor: SequenceEditor,
    rpcConfigs: Configs
) {

  def server() = new SshdRepl(
    SshServerConfig(
      address = "0.0.0.0",
      port = rpcConfigs.replPort,
      passwordAuthenticator = Some(AcceptAllPasswordAuthenticator.INSTANCE) // or publicKeyAuthenticator
    ),
    predef = """
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
      """.stripMargin,
    replArgs = Seq(
      "cs"             -> commandService,
      "sequencer"      -> sequencer,
      "sequenceFeeder" -> sequenceFeeder,
      "sequenceEditor" -> sequenceEditor,
      "supervisor"     -> supervisor
    )
  )
}
