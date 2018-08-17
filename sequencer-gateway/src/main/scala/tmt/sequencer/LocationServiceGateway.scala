package tmt.sequencer

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import csw.messages.location.Connection.{AkkaConnection, TcpConnection}
import csw.messages.location.{AkkaLocation, ComponentId, ComponentType, Location}
import csw.services.command.scaladsl.CommandService
import csw.services.location.scaladsl.LocationService
import io.lettuce.core.RedisURI
import tmt.sequencer.client.{SequenceEditorClient, SequenceFeederClient}
import tmt.sequencer.messages.SupervisorMsg
import tmt.sequencer.util.SequencerUtil

import scala.async.Async.async
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContext, Future}

class LocationServiceGateway(locationService: LocationService)(implicit ec: ExecutionContext, system: ActorSystem) {

  implicit val typedSystem = system.toTyped

  def resolve[T](componentName: String, componentType: ComponentType)(f: AkkaLocation => Future[T]): Future[T] =
    locationService
      .resolve(AkkaConnection(ComponentId(componentName, componentType)), 5.seconds)
      .flatMap {
        case Some(akkaLocation) =>
          f(akkaLocation)
        case None =>
          throw new IllegalArgumentException(s"Could not find component - $componentName of type - $componentType")
      }

  def sequenceFeeder(sequencerId: String, observingMode: String): Future[SequenceFeederClient] = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceFeederClient(supervisorRef)
      }(system.dispatcher)
    }
  }

  def sequenceEditor(sequencerId: String, observingMode: String): Future[SequenceEditorClient] = {
    val componentName = SequencerUtil.getComponentName(sequencerId, observingMode)
    resolve(componentName, ComponentType.Sequencer) { akkaLocation =>
      async {
        val supervisorRef = akkaLocation.actorRef.upcast[SupervisorMsg]
        new SequenceEditorClient(supervisorRef)
      }(system.dispatcher)
    }
  }

  def commandServiceFor(assemblyName: String): Future[CommandService] = {
    resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async(new CommandService(akkaLocation))
    }
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
