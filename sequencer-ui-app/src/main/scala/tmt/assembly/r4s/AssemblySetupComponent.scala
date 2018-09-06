package tmt.assembly.r4s

import com.github.ahnfelt.react4s._
import csw.messages.commands.{CommandName, Observe, Setup}
import csw.messages.params.models.{ObsId, Prefix}
import tmt.assembly.client.AssemblyCommandWebClient
import tmt.sequencer.codecs.SequencerRWSupport
import tmt.sequencer.r4s.theme.{ButtonCss, TextAreaCss}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class AssemblySetupComponent(filterName: P[String], client: P[AssemblyCommandWebClient])
    extends Component[NoEmit]
    with SequencerRWSupport {

  val submitResponse = State("")
  val commandType    = State("")
  val commandName    = State("")

  def handleSubmit(client: AssemblyCommandWebClient, commandType: String, commandName: String): Unit = {
    submitResponse.set("Waiting for Response ....")
    val command = commandType match {
      case "Setup"   => Setup(Prefix("test-prefix"), CommandName(commandName), Some(ObsId("test-obsid")))
      case "Observe" => Observe(Prefix("test-prefix"), CommandName(commandName), Some(ObsId("test-obsid")))
    }
    client.submit(command).onComplete {
      case Success(response) => submitResponse.set(upickle.default.write(response, 2))
      case Failure(ex)       => submitResponse.set(ex.getMessage)
    }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      A.className("card-panel", "hoverable"),
      E.h6(Text("Assembly Setup command")),
      E.div(
        Text("Command Type"),
        E.span(
          E.input(
            A.onChangeText(commandType.set),
            A.value(get(commandType))
          )
        )
      ),
      E.div(
        Text("Command Name"),
        E.span(
          E.input(
            A.onChangeText(commandName.set),
            A.value(get(commandName))
          )
        )
      ),
      E.button(
        ButtonCss,
        Text("Submit"),
        A.onClick { e =>
          e.preventDefault()
          handleSubmit(get(client), get(commandType), get(commandName))
        }
      ),
      E.div(
        Text("Submit Response"),
        E.div(
          TextAreaCss,
          E.span(E.pre(Text(get(submitResponse))))
        )
      )
    )
  }
}
