package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.{SequenceCommandWeb, WebRWSupport}
import tmt.sequencer.ui.r4s.IOOperationComponent
import tmt.sequencer.ui.r4s.IOOperationComponent.HandleClick

import scala.util.{Failure, Success}

case class AddAllComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val addAllResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handleAddAll(get: Get, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      get(client).addAll(upickle.default.read[List[SequenceCommandWeb]](request)).onComplete {
        case Success(_)  => addAllResponse.set("Done")
        case Failure(ex) => addAllResponse.set(ex.getMessage)
      }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(IOOperationComponent, "Sequence Editor - Add All", "Add to Sequence", get(addAllResponse))
        .withHandler(x => handleAddAll(get, x))
    )
  }
}
