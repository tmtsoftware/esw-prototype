package tmt.sequencer.rpc.server

import akka.actor.ActorSystem
import com.typesafe.config.Config

class RpcConfigs(_port: Option[Int])(implicit actorSystem: ActorSystem) {
  private val config: Config = actorSystem.settings.config.getConfig("rpc.server")

  val port: Int = _port.getOrElse(config.getInt("port"))
}
