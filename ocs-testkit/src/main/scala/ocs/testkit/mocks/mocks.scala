package ocs.testkit.mocks

import akka.Done
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{ActorSystem, Cancellable}
import csw.event.api.scaladsl.EventSubscription
import csw.params.commands.{CommandResponse, ControlCommand}
import csw.params.core.models.Id
import csw.params.events.{Event, EventKey}
import ocs.api.SequenceFeeder
import ocs.api.messages.SequencerMsg
import ocs.api.models.{AggregateResponse, CommandList}
import ocs.framework.dsl.CswServices
import ocs.framework.core.{Sequencer, SequencerBehaviour}
import sequencer.macros.StrandEc

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

object SequenceFeederMock extends SequenceFeeder {
  override def feed(commandList: CommandList): Future[Unit] = Future.successful(())
  override def submit(commandList: CommandList): Future[AggregateResponse] = Future.successful(
    AggregateResponse(CommandResponse.Completed(Id("dummy-id")))
  )
}

object EventSubscriptionMock extends EventSubscription {
  override def unsubscribe(): Future[Done] = Future.successful(Done)
  override def ready(): Future[Done]       = Future.successful(Done)
}

object CancellableMock extends Cancellable {
  override def cancel(): Boolean    = true
  override def isCancelled: Boolean = true
}

class CswServicesMock(sequencerId: String, observingMode: String, sequencer: Sequencer)(implicit system: ActorSystem)
    extends CswServices(sequencerId, observingMode, sequencer, null, null, null, null) {
  val commandResponseF: Future[CommandResponse] = Future.successful(CommandResponse.Completed(Id("dummy-id")))

  override def sequenceFeeder(subSystemSequencerId: String): SequenceFeeder                               = SequenceFeederMock
  override def submit(assemblyName: String, command: ControlCommand): Future[CommandResponse]             = commandResponseF
  override def submitAndSubscribe(assemblyName: String, command: ControlCommand): Future[CommandResponse] = commandResponseF
  override def oneway(assemblyName: String, command: ControlCommand): Future[CommandResponse]             = commandResponseF
  override def subscribe(eventKeys: Set[EventKey])(callback: Event => Done)(implicit strandEc: StrandEc): EventSubscription =
    EventSubscriptionMock
  override def publish(every: FiniteDuration)(eventGeneratorBlock: => Event): Cancellable = CancellableMock
  override def publish(event: Event): Future[Done]                                        = Future.successful(Done)
  override def sendResult(msg: String): Future[Done]                                      = Future.successful(Done)
}

object CswServicesMock {
  def create(sequencer: Sequencer)(implicit system: ActorSystem): CswServices =
    new CswServicesMock("sequencer1", "mode1", sequencer)
}

object SequencerFactory {
  def create()(implicit system: ActorSystem): Sequencer = {
    lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
    new Sequencer(sequencerRef, system)
  }
}
