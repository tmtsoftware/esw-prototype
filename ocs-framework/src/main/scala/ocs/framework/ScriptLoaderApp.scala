package ocs.framework
import akka.Done
import akka.actor.CoordinatedShutdown
import csw.location.api.models.{ComponentId, ComponentType}
import csw.params.core.models.Prefix

import scala.concurrent.Future

object ScriptLoaderApp {
  def run(name: String): Unit = {

    val scriptLoaderWiring: ScriptLoaderWiring = new ScriptLoaderWiring(name)

    import scriptLoaderWiring._
    import scriptLoaderWiring.cswSystem._

    locationServiceWrapper.register(
      Prefix("script-loader"),
      ComponentId(name, ComponentType.Service),
      scriptLoaderRef
    )

    CoordinatedShutdown(system).addTask(
      CoordinatedShutdown.PhaseBeforeServiceUnbind,
      "Shutdown redis client"
    ) { () =>
      println("Shutting down redis client")
      Future(redisClient.shutdown()).map(_ => Done)
    }
  }
}
