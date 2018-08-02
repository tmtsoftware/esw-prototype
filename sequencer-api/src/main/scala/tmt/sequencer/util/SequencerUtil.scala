package tmt.sequencer.util

object SequencerUtil {
  val sequencerLocationDelimeter = "@"

  def getComponentName(sequencerId: String, observingMode: String) =
    s"$sequencerId$sequencerLocationDelimeter$observingMode"

  def parseLocation(location: String): String = {
    val sequencerArgs = location.split(sequencerLocationDelimeter)
    val sequencerId   = sequencerArgs(0)
    val observingMode = sequencerArgs(1).split("-")(0)
    s"sequencer/$sequencerId/$observingMode/"
  }
}
