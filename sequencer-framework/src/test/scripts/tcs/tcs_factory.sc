import $file.tcs_dark_night
import tmt.sequencer.ScriptImports._

object TcsFactory {
  def get(cs: CswServices): Script = cs.observingMode match {
    case "darknight"  => new tcs_dark_night.TcsDarkNight(cs)
    case "clearskies" => ???
  }
}
