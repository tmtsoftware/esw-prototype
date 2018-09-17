package tmt.ocs.assembly

import akka.NotUsed
import akka.stream.scaladsl.Source
import csw.params.core.generics.Key
import csw.params.core.generics.KeyType.{IntKey, StringKey}
import csw.params.core.states.CurrentState
import tmt.ocs.models.RequestComponent.{Disperser, FilterWheel}
import tmt.ocs.models.{PositionResponse, RequestComponent}

class PositionTracker(assemblyService: AssemblyService) {

  private val nameKey: Key[String]            = StringKey.make("name")
  private def compKey(name: String): Key[Int] = IntKey.make(s"$name-position")

  def track(assemblyName: String): Source[PositionResponse, NotUsed] = {
    val filterStateStream    = assemblyService.subscribe(assemblyName, Matchers.FilterMatcher)
    val disperserStateStream = assemblyService.subscribe(assemblyName, Matchers.DisperserMatcher)
    filterStateStream.merge(disperserStateStream).map(positionResponse)
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
