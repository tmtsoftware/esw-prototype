package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceFeederClient
import tmt.sequencer.models.{CommandListWeb, WebRWSupport}

import scala.util.{Failure, Success}

case class MainComponent() extends Component[NoEmit] with WebRWSupport {
  val sequence = State("")
  val result   = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  val client = new SequenceFeederClient("http://0.0.0.0:8000")

  def handleClick(get: Get): Unit = {
    val commandListWeb = get(sequence)
    client.feed(upickle.default.read[CommandListWeb](commandListWeb)).onComplete {
      case Success(aggregateResponseWeb) => result.set(upickle.default.write(aggregateResponseWeb, 2))
      case Failure(ex)                   => result.set(ex.getMessage)
    }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      E.textarea(
        TextAreaCss,
        A.onChangeText(sequence.set),
        A.value(get(sequence))
      ),
      E.button(
        ButtonCss,
        A.onClick(e => {
          e.preventDefault()
          handleClick(get)
        }),
        Text("Submit")
      ),
      E.textarea(
        TextAreaCss,
        A.value(get(result))
      )
    )
  }
}

object TextAreaCss
    extends CssClass(
      S.height("400px"),
      S.width("90%"),
      S.float("left"),
      S.display("block"),
      S.fontSize("25px"),
      S.margin("5px 0px 0px 0px")
    )

object ButtonCss
    extends CssClass(
      S.height("45px"),
      S.width("10%"),
      S.margin("20px 0px 0px 15px"),
      S.float("left"),
      S.display("block"),
    )
