package scripts.iris
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import akka.stream.{ActorMaterializer, Materializer}
import csw.location.commons.ClusterSettings
import csw.params.commands.{CommandName, Setup}
import csw.params.core.models.Prefix
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar
import org.tmt.macros.StrandEc
import tmt.ocs.dsl.{CommandDsl, CswServices}
import tmt.ocs.messages.SequencerMsg
import tmt.ocs.{Sequencer, SequencerBehaviour}

import scala.concurrent.ExecutionContext

class IrisDarkNightTest extends FunSuite with MockitoSugar {
  test("should be able to test iris script") {
    lazy val clusterSettings = ClusterSettings()

    lazy implicit val system: ActorSystem                     = clusterSettings.system
    lazy implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped
    lazy implicit val materializer: Materializer              = ActorMaterializer()
    lazy implicit val executionContext: ExecutionContext      = system.dispatcher

    lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
    lazy val sequencer                            = new Sequencer(sequencerRef, system)
    lazy val commandDsl                           = new CommandDsl(sequencer)

    val mockCswServices: CswServices = mock[CswServices]

    val irisDarkNight = new IrisDarkNight(mockCswServices, commandDsl)
    irisDarkNight.execute(Setup(Prefix("sequencer"), CommandName("setup-iris"), None))
  }
}
