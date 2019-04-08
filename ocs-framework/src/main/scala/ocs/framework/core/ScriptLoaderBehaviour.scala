package ocs.framework.core

import akka.Done
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import csw.location.api.models.{ComponentId, ComponentType}
import io.lettuce.core.RedisClient
import ocs.api.SequencerUtil
import ocs.api.messages.ScriptCommand
import ocs.api.messages.ScriptCommand._
import ocs.framework.{CswSystem, Wiring}

object ScriptLoaderBehaviour {

  def behaviour(redisClient: RedisClient, cswSystem: CswSystem): Behavior[ScriptCommand] = {

    lazy val idle: Behavior[ScriptCommand] = Behaviors.receiveMessage[ScriptCommand] {
      case LoadScript(sequencerId, observingMode, sender) =>
        val wiring = new Wiring(sequencerId, observingMode, cswSystem, redisClient)
        wiring.start()
        sender ! Right(Done)
        running(wiring)
      case GetStatus(sender) =>
        sender ! None
        Behaviors.same
      case StopScript(sender) =>
        sender ! Left("ScriptLoader is not running any script")
        Behaviors.same
    }

    def running(wiring: Wiring): Behavior[ScriptCommand] = Behaviors.receiveMessage[ScriptCommand] {
      case StopScript(sender) =>
        wiring.shutDown()
        sender ! Right(Done)
        idle
      case GetStatus(sender) =>
        val componentName = SequencerUtil.getComponentName(wiring.sequencerId, wiring.observingMode)
        sender ! Some(ComponentId(componentName, ComponentType.Sequencer))
        Behaviors.same
      case LoadScript(_, _, sender) =>
        sender ! Left(s"ScriptLoader is running ${wiring.sequencerId} with ${wiring.observingMode}")
        Behaviors.same
    }

    idle
  }
}
