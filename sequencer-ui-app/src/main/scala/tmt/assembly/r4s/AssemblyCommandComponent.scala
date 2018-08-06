package tmt.assembly.r4s

import com.github.ahnfelt.react4s._
import tmt.assembly.client.AssemblyCommandWebClient
import tmt.sequencer.models.{ControlCommandWeb, WebRWSupport}
import tmt.sequencer.r4s.IOOperationComponent
import tmt.sequencer.r4s.IOOperationComponent.HandleClick

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class SubmitComponent(client: P[AssemblyCommandWebClient]) extends Component[NoEmit] with WebRWSupport {

  val submitResponse = State("")

  def handleSubmit(client: AssemblyCommandWebClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      submitResponse.set("Waiting for Response ....")
      client.submit(upickle.default.read[ControlCommandWeb](request)).onComplete {
        case Success(response) => submitResponse.set(upickle.default.write(response, 2))
        case Failure(ex)       => submitResponse.set(ex.getMessage)
      }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(
        IOOperationComponent,
        "Assembly Commands - Submit",
        "Submit Commands",
        get(submitResponse)
      ).withHandler(x => handleSubmit(get(client), x))
    )
  }
}
