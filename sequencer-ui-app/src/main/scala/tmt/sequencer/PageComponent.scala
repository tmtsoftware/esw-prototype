package tmt.sequencer

import com.github.ahnfelt.react4s._
import tmt.WebClients
import tmt.assembly.r4s.AssemblyCommandComponent
import tmt.sequencer.r4s.ListComponent

case class PageComponent(page: P[Page]) extends Component[NoEmit] {
  override def render(get: Get): Node = {
    get(page) match {
      case Home                    => Component(ListComponent, WebClients.listSequencers)
      case SequencerWithMode(_, _) => Component(SequencerComponent)
      case Assembly(_, _)          => Component(AssemblyCommandComponent, WebClients.assemblyCommandClient)
      case _                       => E.div(Text("invalid route"))
    }
  }
}
