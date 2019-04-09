package ocs.framework

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorRef
import csw.location.api.models.{ComponentId, ComponentType}
import csw.params.core.models.Prefix
import io.lettuce.core.RedisClient
import ocs.api.client.ScriptRunnerJvmClient
import ocs.api.messages.ScriptCommand
import ocs.framework.core.ScriptRunner

import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future}

class ScriptRunnerWiring(name: String) {
  lazy val redisClient: RedisClient = RedisClient.create()
  lazy val cswSystem                = new CswSystem("csw-system")
  import cswSystem._

  lazy val scriptRunnerRef: ActorRef[ScriptCommand] =
    Await.result(typedSystem.systemActorOf(ScriptRunner.behaviour(redisClient, cswSystem), name), 5.seconds)

  lazy val scriptRunnerJvmClient = new ScriptRunnerJvmClient(scriptRunnerRef, system)

  def start(): Unit = {
    locationServiceWrapper.register(
      Prefix("script-loader"),
      ComponentId(name, ComponentType.Service),
      scriptRunnerRef
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
