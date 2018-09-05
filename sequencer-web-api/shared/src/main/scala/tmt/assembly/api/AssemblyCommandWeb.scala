package tmt.assembly.api

import csw.messages.commands.ControlCommand
import tmt.sequencer.models.CommandResponseWeb

import scala.concurrent.Future

trait AssemblyCommandWeb {
  def submit(controlCommand: ControlCommand): Future[CommandResponseWeb]
}

object AssemblyCommandWeb {
  val Submit = "Submit"
}
