package tmt.sequencer.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.ListComponentsClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.r4s.theme.OperationTitleCss

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ListComponent(client: P[ListComponentsClient]) extends Component[NoEmit] with WebRWSupport {

  val sequencers: State[List[String]] = State(List.empty[String])
  val assemblies: State[List[String]] = State(List.empty[String])

  def handleListSequencers(client: ListComponentsClient): Unit = client.listSequencers.onComplete {
    case Success(result) => sequencers.set(result)
    case Failure(ex)     => sequencers.set(List(ex.getMessage))
  }

  def handleListAssemblies(client: ListComponentsClient): Unit = client.listAssemblies.onComplete {
    case Success(result) => assemblies.set(result)
    case Failure(ex)     => assemblies.set(List(ex.getMessage))
  }

  override def componentWillRender(get: Get): Unit = {
    handleListSequencers(get(client))
    handleListAssemblies(get(client))
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      Text("Sequencers"),
      E.ul(
        Tags(
          get(sequencers).map(
            sequencer =>
              E.li(
                E.a(
                  A.href(sequencer),
                  Text(sequencer)
                )
            )
          )
        )
      ),
      Text("Assemblies"),
      E.ul(
        Tags(
          get(assemblies).map(
            assembly =>
              E.li(
                E.a(
                  A.href(assembly),
                  Text(assembly)
                )
            )
          )
        )
      )
    )
  }
}
