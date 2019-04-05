package ocs.framework
import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import csw.location.api.models.{ComponentId, ComponentType}
import csw.params.core.models.Prefix
import io.lettuce.core.RedisClient
import ocs.framework.core.ScriptLoaderBehaviour
import ocs.framework.messages.ScriptLoaderMsg

import scala.concurrent.Future

object ScriptLoaderApp {
  def run(name: String): Unit = {
    val redisClient = RedisClient.create()
    val cswSystem   = new CswSystem("csw-system")
    import cswSystem._

    val scriptLoaderRef: ActorRef[ScriptLoaderMsg] =
      system.spawn(ScriptLoaderBehaviour.behaviour(redisClient, cswSystem), name)

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
