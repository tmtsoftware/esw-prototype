package tmt.assembly.r4s

import com.github.ahnfelt.react4s._
import csw.messages.commands.ControlCommand
import tmt.assembly.client.AssemblyCommandWebClient
import tmt.sequencer.codecs.SequencerRWSupport
import tmt.sequencer.r4s.IOOperationComponent
import tmt.sequencer.r4s.IOOperationComponent.HandleClick

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

case class AssemblyCommandComponent(client: P[AssemblyCommandWebClient]) extends Component[NoEmit] with SequencerRWSupport {

  val submitResponse = State("")

  def handleSubmit(client: AssemblyCommandWebClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      submitResponse.set("Waiting for Response ....")
      Try(upickle.default.read[ControlCommand](request))
        .map(
          input =>
            client.submit(input).onComplete {
              case Success(response) => submitResponse.set(upickle.default.write(response, 2))
              case Failure(ex)       => submitResponse.set(ex.getMessage)
          }
        )
        .recover { case ex => submitResponse.set("Invalid input request, please verify the input provided") }
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
