package tmt

import akka.actor.{typed, ActorSystem}
import akka.actor.testkit.typed.scaladsl.TestProbe
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import csw.messages.location.Connection.TcpConnection
import csw.messages.location.{ComponentId, ComponentType}
import csw.services.location.commons.ClusterSettings
import csw.services.location.models.TcpRegistration
import csw.services.location.scaladsl.{LocationService, LocationServiceFactory}
import csw.services.logging.messages.LogControlMessages

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble

object LocationAgentSimulatorApp {
  def main(args: Array[String]): Unit = {
    lazy val clusterSettings                             = ClusterSettings()
    val redisSentinalPort                                = 26379
    implicit val system: ActorSystem                     = clusterSettings.system
    implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

    lazy val locationService: LocationService = LocationServiceFactory.withSystem(system)
    val eventServiceConnection                = TcpConnection(ComponentId("EventServer", ComponentType.Service))
    val probe: TestProbe[LogControlMessages]  = TestProbe[LogControlMessages]
    val tcpRegistration                       = TcpRegistration(eventServiceConnection, redisSentinalPort, probe.ref)
    Await.result(locationService.register(tcpRegistration), 5.seconds)
  }
}
