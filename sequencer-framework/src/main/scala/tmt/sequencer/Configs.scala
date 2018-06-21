package tmt.sequencer

import akka.actor.ActorSystem
import com.typesafe.config.Config

import scala.util.Try

class Configs(sequencerId: String, observingMode: String, _port: Option[Int])(implicit actorSystem: ActorSystem) {
  private lazy val config: Config = actorSystem.settings.config

  lazy val port: Int = _port.getOrElse(config.getConfig("rpc.server").getInt("port"))

  lazy val scriptClass: String =
    Try(
      config.getString(s"scripts.$sequencerId.$observingMode.scriptClass")
    ).toOption
      .getOrElse(
        throw new RuntimeException(s"Please provide script class for $sequencerId in configuration settings")
      )
}
