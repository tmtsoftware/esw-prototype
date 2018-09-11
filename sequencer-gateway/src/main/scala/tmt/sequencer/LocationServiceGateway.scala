package tmt.sequencer

import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import csw.services.command.scaladsl.CommandService
import csw.services.location.api.models.Connection.{AkkaConnection, TcpConnection}
import csw.services.location.api.models.{AkkaLocation, ComponentId, ComponentType, Location}
import csw.services.location.api.scaladsl.LocationService
import io.lettuce.core.RedisURI
import tmt.sequencer.client.{SequenceEditorJvmClient, SequenceFeederJvmClient}
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.util.SequencerUtil

import scala.async.Async.async
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContext, Future}

class LocationServiceGateway(locationService: LocationService)(implicit ec: ExecutionContext, system: ActorSystem) {

  private implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  def resolve[T](componentName: String, componentType: ComponentType)(f: AkkaLocation => Future[T]): Future[T] =
    locationService
      .resolve(AkkaConnection(ComponentId(componentName, componentType)), 5.seconds)
      .flatMap {
        case Some(akkaLocation) =>
          f(akkaLocation)
        case None =>
          throw new IllegalArgumentException(s"Could not find component - $componentName of type - $componentType")
      }

  def sequenceFeeder(sequencerId: String, observingMode: String): Future[SequenceFeederJvmClient] = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederJvmClient(supervisorRef)
      }(system.dispatcher)
    }
  }

  def sequenceEditor(sequencerId: String, observingMode: String): Future[SequenceEditorJvmClient] = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceEditorJvmClient(supervisorRef)
      }(system.dispatcher)
    }
  }

  def commandServiceFor(assemblyName: String): Future[CommandService] = {
    akkaLocationFor(assemblyName).map(new CommandService(_))
  }

  def akkaLocationFor(assemblyName: String): Future[AkkaLocation] = {
    resolve(assemblyName, ComponentType.Assembly)(Future.successful)
  }

  def listSequencers(): Future[List[Location]] = locationService.list(ComponentType.Sequencer)
  def listAssemblies(): Future[List[Location]] = locationService.list(ComponentType.Assembly)

  def redisUrI(masterId: String): Future[RedisURI] = {
    locationService
      .resolve(TcpConnection(ComponentId("EventServer", ComponentType.Service)), 5.seconds)
      .flatMap {
        case Some(tcpLocation) =>
          Future { RedisURI.Builder.sentinel(tcpLocation.uri.getHost, tcpLocation.uri.getPort, masterId).build() }
        case None => throw new IllegalArgumentException(s"Could not find component - Event server")
      }
  }
}
