package ocs.framework.core
import akka.Done
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import csw.location.api.models.{ComponentId, ComponentType}
import io.lettuce.core.RedisClient
import ocs.api.SequencerUtil
import ocs.api.messages.ScriptLoaderMsg
import ocs.api.messages.ScriptLoaderMsg._
import ocs.framework.{CswSystem, Wiring}

object ScriptLoaderBehaviour {
  def behaviour(redisClient: RedisClient, cswSystem: CswSystem): Behavior[ScriptLoaderMsg] =
    Behaviors.setup[ScriptLoaderMsg](
      _ => {
        var wiring: Option[Wiring] = None

        Behaviors.receiveMessage[ScriptLoaderMsg] {
          msg =>
            msg match {
              case LoadScript(sequencerId, observingMode, replyTo) => {
                wiring = Some(new Wiring(sequencerId, observingMode, cswSystem, redisClient))
                wiring.get.start()
                replyTo ! Done
              }
              case StopScript(replyTo) => {
                wiring.get.shutDown()
                wiring = None
                replyTo ! Done
              }
              case GetStatus(replyTo) if wiring.isDefined => {
                val componentName = SequencerUtil.getComponentName(wiring.get.sequencerId, wiring.get.observingMode)
                replyTo ! ComponentId(componentName, ComponentType.Sequencer)
              }
            }
            Behaviors.same
        }
      }
    )
}
