package ocs.framework.core

import akka.Done
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import csw.location.api.models.AkkaLocation
import io.lettuce.core.RedisClient
import ocs.api.messages.ScriptCommand
import ocs.api.messages.ScriptCommand._
import ocs.framework.{CswSystem, Wiring}

object ScriptLoaderBehaviour {

  def behaviour(redisClient: RedisClient, cswSystem: CswSystem): Behavior[ScriptCommand] = {

    lazy val idle: Behavior[ScriptCommand] = Behaviors.receiveMessage[ScriptCommand] {
      case LoadScript(sequencerId, observingMode, sender) =>
        val wiring   = new Wiring(sequencerId, observingMode, cswSystem, redisClient)
        val location = wiring.start()
        sender ! Right(location)
        running(wiring, location)
      case GetStatus(sender) =>
        sender ! None
        Behaviors.same
      case StopScript(sender) =>
        sender ! Done
        Behaviors.same
    }

    def running(wiring: Wiring, location: AkkaLocation): Behavior[ScriptCommand] = Behaviors.receiveMessage[ScriptCommand] {
      case StopScript(sender) =>
        wiring.shutDown()
        sender ! Done
        idle
      case GetStatus(sender) =>
        sender ! Some(location)
        Behaviors.same
      case LoadScript(_, _, sender) =>
        sender ! Left(location)
        Behaviors.same
    }

    idle
  }
}
