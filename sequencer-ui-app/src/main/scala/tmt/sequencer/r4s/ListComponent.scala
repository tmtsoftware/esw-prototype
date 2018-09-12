package tmt.sequencer.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.ListComponentsJsClient
import tmt.ocs.codecs.SequencerJsonSupport
import tmt.sequencer.r4s.theme.ListComponentCss

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ListComponent(client: P[ListComponentsJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val sequencers: State[List[String]] = State(List.empty[String])
  val assemblies: State[List[String]] = State(List.empty[String])

  Get.Unsafe(client).listSequencers.transform {
    case Success(result) => Success(sequencers.set(result))
    case Failure(ex)     => Success(sequencers.set(List(ex.getMessage)))
  }

  Get.Unsafe(client).listAssemblies.transform {
    case Success(result) => Success(assemblies.set(result))
    case Failure(ex)     => Success(assemblies.set(List(ex.getMessage)))
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      E.ul(
        ListComponentCss,
        A.className("collection", "with-header"),
        E.li(
          A.className("collection-header"),
          Text("Sequencers")
        ),
        Tags(
          get(sequencers).map(
            sequencer => E.li(E.a(A.className("collection-item"), A.href(s"#$sequencer"), Text(sequencer)))
          )
        )
      ),
      E.ul(
        ListComponentCss,
        A.className("collection", "with-header"),
        E.li(
          A.className("collection-header"),
          Text("Assemblies")
        ),
        Tags(
          get(assemblies).map(
            assembly => {
              E.li(E.a(A.className("collection-item"), A.href(s"#$assembly"), Text(assembly)))
            }
          )
        ),
        Tags(
          get(assemblies).map(
            assembly => {
              E.li(
                E.a(A.className("collection-item"), A.href(s"#${assembly}filter"), Text(s"Filter Wheel Demo $assembly"))
              )
            }
          )
        )
      )
    )
  }
}
