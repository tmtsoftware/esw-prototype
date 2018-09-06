package tmt.sequencer

sealed trait Page
case object Home                                                       extends Page
case class Sequencers(parent: Home.type)                               extends Page
case class Sequencer(sequencerId: String, parent: Sequencers)          extends Page
case class SequencerWithMode(observingMode: String, parent: Sequencer) extends Page
case class Assemblies(parent: Home.type)                               extends Page
case class Assembly(assemblyName: String, parent: Assemblies)          extends Page
case class FilterAssembly(filterName: String, parent: Assembly)        extends Page
