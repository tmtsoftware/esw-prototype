package tmt.ocs
import com.github.werk.router4s.Extra.string
import com.github.werk.router4s.Router

object Routes {
  val path = new Router[Page]
  val router = path(
    Home,
    path(
      "sequencer",
      Sequencers,
      path(
        string,
        Sequencer,
        path(string, SequencerWithMode)
      )
    ),
    path(
      "assembly",
      Assemblies,
      path(
        string,
        Assembly,
        path(string, FilterAssembly)
      )
    )
  )
}
