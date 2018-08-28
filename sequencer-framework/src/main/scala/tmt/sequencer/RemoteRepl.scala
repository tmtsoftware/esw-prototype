package tmt.sequencer

import akka.actor.typed.ActorRef
import ammonite.sshd._
import org.apache.sshd.server.auth.password.AcceptAllPasswordAuthenticator
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.dsl.CswServices
import tmt.sequencer.messages.SupervisorMsg

// find a workaround for server not hanging after multiple connects/disconnects
// explore timeout thing
class RemoteRepl(commandService: CswServices,
                 sequencer: Sequencer,
                 supervisor: ActorRef[SupervisorMsg],
                 sequenceFeeder: SequenceFeeder,
                 sequenceEditor: SequenceEditor,
                 rpcConfigs: Configs) {

  def server() = new SshdRepl(
    SshServerConfig(
      address = "0.0.0.0",
      port = rpcConfigs.replPort,
      passwordAuthenticator = Some(AcceptAllPasswordAuthenticator.INSTANCE) // or publicKeyAuthenticator
    ),
    predef = """
         |import scala.concurrent.duration.Duration
         |import scala.concurrent.{Await, Future}
         |import csw.messages.params.generics.KeyType._
         |import csw.messages.params.models._
         |import csw.messages.commands._
         |import tmt.sequencer.messages.SequencerMsg._
         |import tmt.sequencer.messages.SupervisorMsg._
         |import tmt.sequencer.models.CommandList
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
