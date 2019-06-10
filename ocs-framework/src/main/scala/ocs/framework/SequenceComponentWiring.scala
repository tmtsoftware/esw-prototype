package ocs.framework

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorRef
import csw.location.api.models.{ComponentId, ComponentType}
import csw.params.core.models.Prefix
import io.lettuce.core.RedisClient
import ocs.api.client.SequenceComponentJvmClient
import ocs.api.messages.SequenceComponentMsg
import ocs.framework.core.SequenceComponent

import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future}

class SequenceComponentWiring(name: String) {
  lazy val redisClient: RedisClient = RedisClient.create()
  lazy val cswSystem                = new CswSystem("csw-system")
  import cswSystem._

  // Q: Why it is created in system namespace?
  // A: On StopScript message, we want to shutdown all the children created with this actor system but not sequenceComponentRef
  //    we do this by shutting down actors in user space
  lazy val sequenceComponentRef: ActorRef[SequenceComponentMsg] =
    Await.result(typedSystem.systemActorOf(SequenceComponent.behaviour(redisClient, cswSystem), name), 5.seconds)

  lazy val sequenceComponentJvmClient = new SequenceComponentJvmClient(sequenceComponentRef, system)

  def start(): Unit = {
    CoordinatedShutdown(system).addTask(
      CoordinatedShutdown.PhaseBeforeServiceUnbind,
      "Shutdown redis client"
    ) { () =>
      println("Shutting down redis client")
      Future(redisClient.shutdown()).map(_ => Done)
    }

    Await.result(
      locationServiceWrapper.register(Prefix("script-loader"), ComponentId(name, ComponentType.Service), sequenceComponentRef),
      5.seconds
    )

    println("script-runner is started")
  }

}
