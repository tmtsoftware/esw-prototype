package ocs.react4s.app

object Routes {
  import trail._

  val home: String       = Root.url(())
  val sequencerInfo      = Root / "sequencer" / Arg[String] / Arg[String]
  val assemblyInfo       = Root / "assembly" / Arg[String]
  val filterAssemblyInfo = Root / "assembly" / Arg[String] / Arg[String]
}
