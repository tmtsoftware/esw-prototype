package tmt.sequencer

import akka.actor.ActorSystem
import com.typesafe.config.Config

class Configs(_port: Option[Int])(implicit actorSystem: ActorSystem) {
  private lazy val config: Config = actorSystem.settings.config

  lazy val port: Int = _port.getOrElse(config.getConfig("rpc.server").getInt("port"))
}
