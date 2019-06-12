package ocs.gateway.server

import akka.actor
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.actor.typed.{ActorSystem, SpawnProtocol}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult._
import akka.stream.typed.scaladsl.ActorMaterializer
import ocs.gateway.Configs

import scala.concurrent.Future

class Server(configs: Configs, routes: Routes)(implicit typedSystem: ActorSystem[SpawnProtocol]) {
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  import materializer.executionContext
  implicit val unTypedSystem: actor.ActorSystem = typedSystem.toUntyped

  def start(): Future[Http.ServerBinding] = Http().bindAndHandle(routes.route, interface = "0.0.0.0", port = configs.port)
}
