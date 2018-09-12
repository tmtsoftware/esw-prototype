package tmt.ocs

object SequencerUtil {
  val sequencerLocationDelimeter = "@"

  def getComponentName(sequencerId: String, observingMode: String) =
    s"$sequencerId$sequencerLocationDelimeter$observingMode"

  def parseSequencerLocation(location: String): String = {
    val sequencerArgs = location.split(sequencerLocationDelimeter)
    val sequencerId   = sequencerArgs(0)
    val observingMode = sequencerArgs(1).split("-")(0)
    s"sequencer/$sequencerId/$observingMode/"
  }

  def parseAssemblyLocation(location: String): String = {
    val assemblyArgs = location.split("-")
    s"assembly/${assemblyArgs(0)}/"
  }
}
