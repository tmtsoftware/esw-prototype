package tmt.sequencer.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceEditorClient
import tmt.sequencer.codecs.SequencerJsonSupport
import tmt.sequencer.r4s.theme.ButtonCss

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ResetComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val resetResponse = State("")

  def handleReset(client: SequenceEditorClient): Unit = client.reset().onComplete {
    case Success(_)  => resetResponse.set("Operation Successful")
    case Failure(ex) => resetResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      A.className("card-panel", "hoverable"),
      E.h6(Text("Sequence Editor Reset")),
      E.button(
        ButtonCss,
        Text("Reset Sequence"),
        A.onClick { e =>
          e.preventDefault()
          handleReset(get(client))
        }
      ),
      Text(get(resetResponse))
    )
  }
}
