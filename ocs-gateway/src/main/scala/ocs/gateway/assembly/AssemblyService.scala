package ocs.gateway.assembly

import akka.NotUsed
import akka.actor.typed.scaladsl.adapter._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import akka.util.Timeout
import csw.command.api.scaladsl.StateMatcher
import csw.command.client.internal.messages.ComponentCommonMessage.ComponentStateSubscription
import csw.command.client.internal.messages.ComponentMessage
import csw.command.client.internal.models.framework.PubSub.Subscribe
import csw.params.commands.{CommandName, CommandResponse, Setup}
import csw.params.core.generics.KeyType.StringKey
import csw.params.core.models.Prefix
import csw.params.core.states.CurrentState
import ocs.api.models.RequestComponent
import ocs.factory.{ComponentFactory, LocationServiceWrapper}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class AssemblyService(locationServiceGateway: LocationServiceWrapper, componentFactory: ComponentFactory)(
    implicit ec: ExecutionContext
) {

  private implicit val timeout: Timeout = Timeout(10.seconds)
  private val prefix                    = Prefix("sequencer1")
  private val nameKey                   = StringKey.make("name")

  def oneway(assemblyName: String, component: RequestComponent): Future[CommandResponse] = {
    componentFactory.assembly(assemblyName).flatMap { cs =>
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
