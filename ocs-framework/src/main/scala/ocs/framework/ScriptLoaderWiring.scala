package ocs.framework
import akka.actor.typed.ActorRef
import io.lettuce.core.RedisClient
import ocs.api.client.ScriptLoaderCommandServiceJvmClient
import ocs.api.messages.ScriptLoaderMsg
import ocs.framework.core.ScriptLoaderBehaviour

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

class ScriptLoaderWiring(name: String) {
  val redisClient: RedisClient = RedisClient.create()
  val cswSystem                = new CswSystem("csw-system")
  import cswSystem._

  val scriptLoaderRef: ActorRef[ScriptLoaderMsg] =
    Await.result(typedSystem.systemActorOf(ScriptLoaderBehaviour.behaviour(redisClient, cswSystem), name), 5.seconds)

  val scriptLoaderCommandService = new ScriptLoaderCommandServiceJvmClient(scriptLoaderRef, system)
}
