package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.{SequenceCommandWeb, WebRWSupport}
import tmt.sequencer.ui.r4s.{IOOperationComponent, SequencerConstants}
import tmt.sequencer.ui.r4s.IOOperationComponent.HandleClick

import scala.util.{Failure, Success}

case class PrependComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val prependResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handlePrepend(client: SequenceEditorClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      client.prepend(upickle.default.read[List[SequenceCommandWeb]](request)).onComplete {
        case Success(_)  => prependResponse.set(SequencerConstants.SUCCESS_MSG)
        case Failure(ex) => prependResponse.set(ex.getMessage)
      }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(IOOperationComponent,
                SequencerConstants.PREPEND_COMPONENT,
                SequencerConstants.PREPEND_OPERATION,
                get(prependResponse))
        .withHandler(x => handlePrepend(get(client), x))
    )
  }
}
