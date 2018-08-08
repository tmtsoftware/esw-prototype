package tmt
import com.github.ahnfelt.react4s._
import tmt.assembly.AssemblyCommandComponent
import tmt.sequencer.editor.EditorComponent
import tmt.sequencer.feeder.FeederComponent
import tmt.sequencer.resultevent.ResultEventComponent

case class PageComponent(page: P[Page]) extends Component[NoEmit] {
  override def render(get: Get): Node = {
    get(page) match {
      case Home                    => renderHomePage
      case Sequencers(_)           => renderInvalidRoute
      case Sequencer(_, _)         => renderInvalidRoute
      case SequencerWithMode(_, _) => renderSequencerWithMode
      case Assemblies(_)           => renderInvalidRoute
      case Assembly(_, _)          => renderAssembly
      case _                       => renderInvalidRoute

    }
  }

  def renderHomePage: Node = {
    Component(ListComponent, WebClients.listSequencers)
  }

  def renderSequencerWithMode: Element = {
    E.div(
      Component(HeaderComponent),
      Component(FeederComponent, WebClients.feeder),
      Component(EditorComponent, WebClients.editor),
      Component(ResultEventComponent, WebClients.logger)
    )
  }

  def renderAssembly: Node = {
    Component(AssemblyCommandComponent, WebClients.assemblyCommandClient)
  }

  def renderInvalidRoute: Element = {
    E.div(Text("invalid route"))
  }
}
