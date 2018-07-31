package tmt.sequencer.rpc.server

import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.{typed, ActorSystem, CoordinatedShutdown}
import csw.messages.location.Connection.AkkaConnection
import csw.messages.location.{AkkaLocation, ComponentId, ComponentType}
import csw.messages.params.models.Prefix
import csw.services.location.commons.ActorSystemFactory
import csw.services.location.models.AkkaRegistration
import csw.services.location.scaladsl.LocationService
import csw.services.logging.messages.LogControlMessages
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.client.{SequenceEditorImpl, SequenceFeederImpl}
import tmt.sequencer.messages.SupervisorMsg

import scala.async.Async.async
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, ExecutionContext, Future}

class LocationServiceGateway(locationService: LocationService, system: ActorSystem)(implicit ec: ExecutionContext) {

  def register(componentName: String, componentType: ComponentType, supervisorRef: ActorRef[SupervisorMsg]): Unit = {
    val dummyLogAdminActorRef: typed.ActorRef[LogControlMessages] =
      ActorSystemFactory.remote().spawn(Behavior.empty, "dummy-log-admin-actor-ref")

    val registration =
      AkkaRegistration(AkkaConnection(ComponentId(componentName, componentType)),
                       Prefix("sequencer"),
                       supervisorRef,
                       dummyLogAdminActorRef)

    println(s"Registering [${registration.logAdminActorRef.path}]")
    locationService.register(registration).foreach { registrationResult =>
      println(s"Successfully registered $componentName - $registrationResult")

      CoordinatedShutdown(system).addTask(
        CoordinatedShutdown.PhaseBeforeServiceUnbind,
        s"unregistering-${registrationResult.location}"
      ) { () =>
        println(s"Shutting down actor system, unregistering-${registrationResult.location}")
        registrationResult.unregister()
      }
    }

  }

  def resolve[T](componentName: String, componentType: ComponentType)(f: AkkaLocation => Future[T]): Future[T] =
    locationService
      .resolve(AkkaConnection(ComponentId(componentName, componentType)), 5.seconds)
      .flatMap {
        case Some(akkaLocation) =>
          f(akkaLocation)
        case None =>
          throw new IllegalArgumentException(s"Could not find component - $componentName of type - $componentType")
      }

  def sequenceFeeder(sequencerId: String, observingMode: String): SequenceFeeder = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    val eventualFeederImpl = resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederImpl(supervisorRef)(system)
      }(system.dispatcher)
    }
    Await.result(eventualFeederImpl, 5.seconds)
  }

  def sequenceEditor(sequencerId: String, observingMode: String): SequenceEditor = {
    val componentName = SequencerComponent.getComponentName(sequencerId, observingMode)
    val eventualEditorImpl = resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceEditorImpl(supervisorRef)(system)
      }(system.dispatcher)
    }
    Await.result(eventualEditorImpl, 5.seconds)
  }
}
