package ocs.react4s.app

import com.github.ahnfelt.react4s._
import ocs.api.client.ListComponentsJsClient
import ocs.api.codecs.SequencerJsonSupport
import ocs.react4s.app.theme.ListComponentCss

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
        )
      ),
      E.ul(
        ListComponentCss,
        A.className("collection", "with-header"),
        E.li(
          A.className("collection-header"),
          Text("Demo App")
        ),
        Tags(
          get(assemblies).map(
            assembly => {
              E.li(
                E.a(A.className("collection-item"), A.href(s"#${assembly}filter"), Text(assembly))
              )
            }
          )
        )
      )
    )
  }
}
