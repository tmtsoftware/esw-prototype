package ocs.framework
import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorRef
import csw.location.api.models.{ComponentId, ComponentType}
import csw.params.core.models.Prefix
import io.lettuce.core.RedisClient
import ocs.api.messages.ScriptLoaderMsg
import ocs.framework.core.ScriptLoaderBehaviour

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object ScriptLoaderApp {
  def run(name: String): Unit = {
    val redisClient = RedisClient.create()
    val cswSystem   = new CswSystem("csw-system")
    import cswSystem._

    val scriptLoaderRef: ActorRef[ScriptLoaderMsg] =
      Await.result(typedSystem.systemActorOf(ScriptLoaderBehaviour.behaviour(redisClient, cswSystem), name), 5.seconds)

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
