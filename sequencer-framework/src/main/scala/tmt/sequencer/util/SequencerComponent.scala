package tmt.sequencer.util

import csw.messages.commands.{CommandName, Observe, Setup, Wait}
import csw.messages.params.models.{ObsId, Prefix}
import tmt.sequencer.models.Command

object SequencerComponent {
  def getComponentName(sequencerId: String, observingMode: String) = s"$sequencerId@$observingMode"
}

object CswCommandAdapter {
  def setupCommandFrom(setup: Command): Setup = {
    import setup._
    Setup(Prefix(prefix), CommandName(commandName), maybeObsId.map(ObsId(_)))
  }

  def observeCommandFrom(observe: Command): Observe = {
    import observe._
    Observe(Prefix(prefix), CommandName(commandName), maybeObsId.map(ObsId(_)))
  }

  def waitCommandFrom(wait: Command): Wait = {
    import wait._
    Wait(Prefix(prefix), CommandName(commandName), maybeObsId.map(ObsId(_)))
  }

}
