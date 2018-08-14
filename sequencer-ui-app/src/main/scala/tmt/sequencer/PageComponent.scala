package tmt.sequencer

import com.github.ahnfelt.react4s._
import tmt.WebClients
import tmt.assembly.r4s.AssemblyCommandComponent
import tmt.sequencer.models.SequencerInfo
import tmt.sequencer.r4s.ListComponent

case class PageComponent(page: P[Page]) extends Component[NoEmit] {
  override def render(get: Get): Node = {
    get(page) match {
      case Home                                      => Component(ListComponent, WebClients.listSequencers)
      case SequencerWithMode(mode, Sequencer(id, _)) => Component(SequencerComponent, SequencerInfo(id, mode))
      case Assembly(assemblyName, _)                 => Component(AssemblyCommandComponent, WebClients.assemblyCommandClient(assemblyName))
      case _                                         => E.div(Text("invalid route"))
    }
  }
}
