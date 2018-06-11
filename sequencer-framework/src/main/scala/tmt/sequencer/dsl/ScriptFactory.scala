package tmt.sequencer.dsl

trait ScriptFactory {
  def get(cs: CswServices): Script
}
