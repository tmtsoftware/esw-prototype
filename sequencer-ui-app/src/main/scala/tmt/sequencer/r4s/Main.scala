package tmt.sequencer.r4s

import com.github.ahnfelt.react4s._
import org.scalajs.dom.window
import tmt.WebClients
import tmt.assembly.r4s.AssemblyCommandComponent
import tmt.sequencer.r4s.editor.EditorComponent
import tmt.sequencer.r4s.facade.NpmReactBridge
import tmt.sequencer.r4s.feeder.FeederComponent
import tmt.sequencer.r4s.resultevent.ResultEventComponent

object Main {
  def main(arguments: Array[String]): Unit = {
    val component = if (window.location.pathname == "/") {
      Component(ListComponent, WebClients.listSequencers)
    } else if (window.location.pathname.contains("/sequencer/")) {
      E.div(
        Component(HeaderComponent),
        Component(FeederComponent, WebClients.feeder),
        Component(EditorComponent, WebClients.editor),
        Component(ResultEventComponent, WebClients.logger)
      )
    } else {
      Component(AssemblyCommandComponent, WebClients.assemblyCommandClient)
    }
    NpmReactBridge.renderToDomById(component, "main")
  }
}
