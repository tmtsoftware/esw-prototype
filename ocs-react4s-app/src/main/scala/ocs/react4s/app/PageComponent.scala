package ocs.react4s.app

import com.github.ahnfelt.react4s._
import ocs.api.WebClients
import ocs.api.models.SequencerInfo
import ocs.react4s.app.assembly.{AssemblyCommandComponent, AssemblySetupComponent}

case class PageComponent(page: P[String]) extends Component[NoEmit] {
  import shapeless._

  override def render(get: Get): Node = {
    get(page) match {
      case Routes.home => Component(ListComponent, WebClients.listSequencers)

      case Routes.assemblyInfo(name :: HNil) =>
        Component(AssemblyCommandComponent, WebClients.assemblyCommandClient(name.toString))

      case Routes.sequencerInfo(id :: mode :: HNil) =>
        Component(SequencerComponent, SequencerInfo(id.toString, mode.toString))

      case Routes.filterAssemblyInfo(assemblyName :: filterName :: HNil) =>
        Component(AssemblySetupComponent, filterName.toString, WebClients.assemblyCommandClient(assemblyName.toString))

      case _ => E.div(Text("invalid route"))
    }
  }
}
