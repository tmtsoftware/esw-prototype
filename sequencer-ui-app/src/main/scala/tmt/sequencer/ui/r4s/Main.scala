package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.editor.EditorComponent
import tmt.sequencer.ui.r4s.facade.NpmReactBridge
import tmt.sequencer.ui.r4s.feeder.FeederComponent
import tmt.sequencer.ui.r4s.logevent.LogEventComponent
import tmt.sequencer.{SequenceEditorClient, SequenceFeederClient, SequenceLoggerClient, WebGateway}

object Main {
  def main(arguments: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val gateway                       = new WebGateway()
    val clientF: SequenceFeederClient = new SequenceFeederClient()
    val clientE: SequenceEditorClient = new SequenceEditorClient(gateway)
    val clientL: SequenceLoggerClient = new SequenceLoggerClient()

    val component = E.div(
      Component(HeaderComponent),
      Component(FeederComponent, clientF),
      Component(EditorComponent, clientE),
      Component(LogEventComponent, clientL)
    )
    NpmReactBridge.renderToDomById(component, "main")
  }
}
