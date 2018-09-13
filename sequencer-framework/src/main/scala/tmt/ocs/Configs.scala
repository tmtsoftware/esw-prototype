package tmt.ocs

import akka.actor.ActorSystem
import com.typesafe.config.Config

import scala.util.Try

class Configs(sequencerId: String, observingMode: String, _replPort: Int)(implicit actorSystem: ActorSystem) {
  private lazy val config: Config = actorSystem.settings.config

  lazy val replPort: Int = _replPort

  lazy val scriptClass: String =
    Try(
      config.getString(s"scripts.$sequencerId.$observingMode.scriptClass")
    ).toOption
      .getOrElse(
        throw new RuntimeException(s"Please provide script class for $sequencerId in configuration settings")
      )
}
