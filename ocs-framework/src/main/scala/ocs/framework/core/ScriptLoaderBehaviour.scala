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

        def receive(wiring: Option[Wiring]): Behavior[ScriptLoaderMsg] = {
          Behaviors.receiveMessage[ScriptLoaderMsg] {
            case LoadScript(sequencerId, observingMode, replyTo) => {
              val newWiring = Some(new Wiring(sequencerId, observingMode, cswSystem, redisClient))
              newWiring.get.start()
              replyTo ! Done
              receive(newWiring)
            }
            case StopScript(replyTo) if wiring.isDefined => {
              wiring.get.shutDown()
              replyTo ! Done
              receive(None)
            }
            case GetStatus(replyTo) if wiring.isDefined => {
              val componentName = SequencerUtil.getComponentName(wiring.get.sequencerId, wiring.get.observingMode)
              replyTo ! ComponentId(componentName, ComponentType.Sequencer)
              receive(wiring)
            }
          }
        }
        receive(None)
      }
    )
}
