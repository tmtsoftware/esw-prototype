package tmt.sequencer

import akka.actor.ActorSystem
import com.typesafe.config.Config

class ScriptConfigs(actorSystem: ActorSystem) {

  private val config: Config = actorSystem.settings.config.getConfig("scripts")

  val scriptFactoryPath: String = config.getString("script-factory-path")
}
