package tmt.sequencer.assembly

import akka.NotUsed
import akka.stream.scaladsl.{Merge, Source}
import csw.messages.params.generics.Key
import csw.messages.params.generics.KeyType.{IntKey, StringKey}
import csw.messages.params.states.CurrentState
import tmt.assembly.models.{PositionResponse, RequestComponent}
import tmt.assembly.models.RequestComponent.{Disperser, FilterWheel}

class PositionTracker(assemblyService: AssemblyService) {

  private val nameKey: Key[String]            = StringKey.make("name")
  private def compKey(name: String): Key[Int] = IntKey.make(s"$name-position")

  def track(assemblyName: String): Source[PositionResponse, NotUsed] = {
    val stream1 = assemblyService.subscribe(assemblyName, Matchers.FilterMatcher)
    val stream2 = assemblyService.subscribe(assemblyName, Matchers.DisperserMatcher)
    Source.combine(stream1, stream2)(Merge(_)).map(positionResponse)
  }

  def positionResponse(currentState: CurrentState): PositionResponse = {
    val compName = currentState.stateName.name
    val position = currentState.get(compKey(compName)).flatMap(_.get(0)).getOrElse(0)
    val requestComponent: RequestComponent = compName match {
      case "filter"    => FilterWheel(currentState.get(nameKey).flatMap(_.get(0)).getOrElse("unknown"))
      case "disperser" => Disperser(currentState.get(nameKey).flatMap(_.get(0)).getOrElse("unknown"))
    }

    PositionResponse(requestComponent, position)
  }
}
