package tmt.assembly.models

sealed trait RequestComponent
object RequestComponent {
  case class FilterWheel(name: String) extends RequestComponent
  case class Disperser(name: String)   extends RequestComponent
}

case class PositionResponse(requestComponent: RequestComponent, position: Int)
