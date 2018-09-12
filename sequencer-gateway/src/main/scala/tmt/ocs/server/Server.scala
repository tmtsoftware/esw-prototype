package tmt.ocs.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult._
import akka.stream.ActorMaterializer
import tmt.ocs.Configs

import scala.concurrent.Future

class Server(configs: Configs, routes: Routes)(implicit system: ActorSystem) {
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  import materializer.executionContext

  def start(): Future[Http.ServerBinding] = Http().bindAndHandle(routes.route, interface = "0.0.0.0", port = configs.port)
}
