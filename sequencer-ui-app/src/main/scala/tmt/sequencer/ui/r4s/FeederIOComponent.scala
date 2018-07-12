package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s.{Text, _}
import tmt.sequencer.SequenceFeederClient
import tmt.sequencer.models.{AggregateResponseWeb, CommandListWeb, WebRWSupport}
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss, TextAreaCss}

import scala.util.{Failure, Success}

case class FeederIOComponent(api: P[String], path: P[String], client: P[SequenceFeederClient])
    extends Component[NoEmit]
    with WebRWSupport {
  val input = State(
    """{
  "commands": [
    {
        "kind": "Setup",
        "source": "test1",
        "commandName": "setup-iris",
        "maybeObsId": [
          "test-obsId1"
        ],
        "paramSet": [
          {
            "keyName": "myStruct",
            "keyType": "StructKey",
            "values": [
              {
                 "paramSet": [
                   {
                        "keyName": "ra",
                        "keyType": "StringKey",
                          "values": [
                            "12:13:14.1"
                          ],
                          "units": "NoUnits"
                        },
                      {
                          "keyName": "dec",
                          "keyType": "StringKey",
                          "values": [
                            "32:33:34.4"
                          ],
                          "units": "NoUnits"
                        },
                      {
                          "keyName": "epoch",
                          "keyType": "DoubleKey",
                          "values": [
                            1950.0
                          ],
                          "units": "NoUnits"
                        }
                    ]
                  }
            ],
              "units": "NoUnits"
            }
        ]
      }
  ]
  }""".stripMargin
  )

  val output: State[String] = State("Command Response from sequencer will be displayed here")

  import scala.concurrent.ExecutionContext.Implicits.global

  def loadingOutput(): Unit = output.set("Waiting for Command Response ....")

  def handleClick(get: Get): Unit = {
    val postData = get(input)
    loadingOutput()
    get(client).feed(upickle.default.read[CommandListWeb](postData)).onComplete {
      case Success(res) => output.set(upickle.default.write[AggregateResponseWeb](res, 2))
      case Failure(ex)  => output.set(ex.getMessage)
    }
  }

  override def render(get: Get): Node = {

    E.div(
      E.div(
        OperationTitleCss,
        Text(get(api)),
        E.div(
          E.span(
            E.textarea(
              TextAreaCss,
              S.height.px(280),
              A.onChangeText(input.set),
              A.value(get(input))
            )
          )
        ),
        E.div(
          E.button(ButtonCss, Text(get(path)), A.onClick(e => {
            e.preventDefault()
            handleClick(get)
          }))
        )
      ),
      E.div(OperationTitleCss,
            Text(s"${get(api)} Response"),
            E.div(
              TextAreaCss,
              E.span(
                E.pre(
                  Text(
                    get(output)
                  )
                )
              )
            ))
    )

  }

}
