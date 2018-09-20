package scripts.mocks
import akka.Done
import akka.actor.ActorSystem
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import csw.event.api.scaladsl.EventSubscription
import csw.params.commands.{CommandResponse, ControlCommand, SequenceCommand}
import csw.params.core.models.Id
import csw.params.events.{Event, EventKey}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import org.tmt.macros.StrandEc
import tmt.ocs.dsl.CswServices
import tmt.ocs.messages.SequencerMsg
import tmt.ocs.{Sequencer, SequencerBehaviour}

import scala.concurrent.Future

object CswServicesMock extends Matchers with MockitoSugar {
  def createMock: CswServices = {
    val mockCswServices      = mock[CswServices]
    val dummyCommandResponse = CommandResponse.Completed(Id("dummy-id"))
    val mockSequenceFeeder   = SequenceFeederMock.createMock

    when(mockCswServices.oneway(any[String], any[ControlCommand])).thenReturn(Future.successful(dummyCommandResponse))
    when(mockCswServices.submit(any[String], any[ControlCommand])).thenReturn(Future.successful(dummyCommandResponse))
    when(mockCswServices.submitAndSubscribe(any[String], any[ControlCommand])).thenReturn(Future.successful(dummyCommandResponse))
    when(mockCswServices.setup(any[String], any[SequenceCommand])).thenReturn(Future.successful(dummyCommandResponse))
    when(mockCswServices.sequenceFeeder(any[String])).thenReturn(mockSequenceFeeder)
    when(mockCswServices.publish(any[Event])).thenReturn(Future.successful(Done))
    when(mockCswServices.sendResult(any[String])).thenReturn(Future.successful(Done))
    when(mockCswServices.sequencer).thenReturn(getSequencer)
    when(mockCswServices.subscribe(any[Set[EventKey]])(any())(any[StrandEc]))
      .thenReturn(new EventSubscription {
        override def unsubscribe(): Future[Done] = Future.successful(Done)
        override def ready(): Future[Done]       = Future.successful(Done)
      })

    mockCswServices
  }

  private def getSequencer: Sequencer = {
    lazy implicit val system: ActorSystem         = ActorSystem("test")
    lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
    new Sequencer(sequencerRef, system)
  }

}
