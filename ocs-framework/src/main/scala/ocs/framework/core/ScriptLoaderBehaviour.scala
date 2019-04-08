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
    def running(wiring: Wiring): Behavior[ScriptCommand] =
      Behaviors.receiveMessage[ScriptCommand] {
        case StopScript(replyTo) =>
          wiring.shutDown()
          replyTo ! Right(Done)
          idle
        case GetStatus(replyTo) =>
          val componentName = SequencerUtil.getComponentName(wiring.sequencerId, wiring.observingMode)
          replyTo ! Right(ComponentId(componentName, ComponentType.Sequencer))
          running(wiring)
        case LoadScript(_, _, replyTo) =>
          replyTo ! Left(s"ScriptLoader is running ${wiring.sequencerId} with ${wiring.observingMode}")
          Behaviors.same
      }

    lazy val idle: Behavior[ScriptCommand] = {
      Behaviors.receiveMessage[ScriptCommand] {
        case LoadScript(sequencerId, observingMode, replyTo) =>
          val newWiring = new Wiring(sequencerId, observingMode, cswSystem, redisClient)
          newWiring.start()
          replyTo ! Right(Done)
          running(newWiring)
        case StopScript(replyTo) =>
          replyTo ! Left("ScriptLoader is not running any script")
          Behaviors.same
        case GetStatus(replyTo) =>
          replyTo ! Left("ScriptLoader is not running any script")
          Behaviors.same
      }
    }

    idle
  }
}
