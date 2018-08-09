package tmt
import com.github.ahnfelt.react4s._
import tmt.assembly.AssemblyCommandComponent

case class PageComponent(page: P[Page]) extends Component[NoEmit] {
  override def render(get: Get): Node = {
    get(page) match {
      case Home                 => Component(ListComponent, WebClients.listSequencers)
      case x: SequencerWithMode => Component(SequencerComponent)
      case x: Assembly          => Component(AssemblyCommandComponent, WebClients.assemblyCommandClient)
      case _                    => E.div(Text("invalid route"))
    }
  }
}
