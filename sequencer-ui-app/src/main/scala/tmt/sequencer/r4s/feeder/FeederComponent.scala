package tmt.sequencer.r4s.feeder

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceFeederWebClient
import tmt.sequencer.models.{CommandListWeb, WebRWSupport}
import tmt.sequencer.r4s.IOOperationComponent
import tmt.sequencer.r4s.IOOperationComponent.HandleClick

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class FeederComponent(client: P[SequenceFeederWebClient]) extends Component[NoEmit] with WebRWSupport {

  val feedResponse = State("")

  def handleFeed(client: SequenceFeederWebClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      feedResponse.set("Waiting for Response ....")
      client.feed(upickle.default.read[CommandListWeb](request)).onComplete {
        case Success(response) => feedResponse.set("Operation Successful")
        case Failure(ex)       => feedResponse.set(ex.getMessage)
      }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(
        IOOperationComponent,
        "Sequence Feeder - Feed",
        "Feed Sequence",
        get(feedResponse)
      ).withHandler(x => handleFeed(get(client), x))
    )
  }
}
