package ocs.gateway

import akka.actor.typed.{ActorSystem, SpawnProtocol}
import com.typesafe.config.Config

class Configs(_port: Option[Int])(implicit actorSystem: ActorSystem[SpawnProtocol]) {
  private lazy val config: Config = actorSystem.settings.config

  lazy val port: Int = _port.getOrElse(config.getConfig("server").getInt("port"))
}
