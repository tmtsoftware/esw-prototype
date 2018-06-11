package tmt.sequencer.rpc.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult._
import akka.stream.ActorMaterializer

import scala.concurrent.Future

class RpcServer2(rpcConfigs: RpcConfigs, routes: Routes2)(implicit system: ActorSystem) {
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  import materializer.executionContext

  def start(): Future[Http.ServerBinding] = Http().bindAndHandle(routes.route, interface = "0.0.0.0", port = rpcConfigs.port)
}
