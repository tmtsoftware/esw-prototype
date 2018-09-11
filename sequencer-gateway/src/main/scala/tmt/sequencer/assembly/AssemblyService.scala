package tmt.sequencer.assembly

import akka.NotUsed
import akka.actor.typed.scaladsl.adapter._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import akka.util.Timeout
import csw.messages.ComponentCommonMessage.ComponentStateSubscription
import csw.messages.ComponentMessage
import csw.messages.commands.matchers.StateMatcher
import csw.messages.commands.{CommandName, CommandResponse, Setup}
import csw.messages.framework.PubSub.Subscribe
import csw.messages.params.generics.KeyType.StringKey
import csw.messages.params.models.Prefix
import csw.messages.params.states.CurrentState
import tmt.assembly.models.RequestComponent
import tmt.sequencer.LocationServiceGateway

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class AssemblyService(locationServiceGateway: LocationServiceGateway)(implicit ec: ExecutionContext) {

  private implicit val timeout: Timeout = Timeout(10.seconds)
  private val prefix                    = Prefix("sequencer1")
  private val nameKey                   = StringKey.make("name")

  def oneway(assemblyName: String, component: RequestComponent): Future[CommandResponse] = {
    locationServiceGateway.commandServiceFor(assemblyName).flatMap { cs =>
      component match {
        case RequestComponent.FilterWheel(name) =>
          cs.oneway(Setup(prefix, CommandName("filter-move"), None, Set(nameKey.set(name))))
        case RequestComponent.Disperser(name) =>
          cs.oneway(Setup(prefix, CommandName("disperser-move"), None, Set(nameKey.set(name))))
      }
    }
  }

  def subscribe(assemblyName: String, stateMatcher: StateMatcher): Source[CurrentState, NotUsed] = {
    Source
      .fromFuture(locationServiceGateway.akkaLocationFor(assemblyName))
      .flatMapConcat { akkaLocation =>
        Source
          .actorRef[CurrentState](256, OverflowStrategy.fail)
          .mapMaterializedValue { ref ⇒
            akkaLocation.typedRef[ComponentMessage] ! ComponentStateSubscription(Subscribe(ref))
          }
      }
      .filter(cs ⇒ cs.stateName.name == stateMatcher.stateName && cs.prefixStr == stateMatcher.prefix)
      .takeWhile(x => !stateMatcher.check(x), inclusive = true)
  }
}
