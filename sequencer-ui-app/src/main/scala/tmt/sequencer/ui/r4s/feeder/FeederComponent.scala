package tmt.sequencer.ui.r4s.feeder

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceFeederClient
import tmt.sequencer.models.{CommandListWeb, WebRWSupport}
import tmt.sequencer.ui.r4s.IOOperationComponent.HandleClick
import tmt.sequencer.ui.r4s.{IOOperationComponent, SequencerConstants}

import scala.util.{Failure, Success}

case class FeederComponent(client: P[SequenceFeederClient]) extends Component[NoEmit] with WebRWSupport {
  val feedResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def loadingOutput(): Unit = feedResponse.set(SequencerConstants.LOADER_TEXT)

  def handleFeed(client: SequenceFeederClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      loadingOutput()
      client.feed(upickle.default.read[CommandListWeb](request)).onComplete {
        case Success(response) => feedResponse.set(upickle.default.write(response, 2))
        case Failure(_)        => feedResponse.set(SequencerConstants.ERROR_MSG)
      }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(IOOperationComponent, SequencerConstants.FEED_COMPONENT, SequencerConstants.FEED_OPERATION, get(feedResponse))
        .withHandler(x => handleFeed(get(client), x))
    )
  }
}
