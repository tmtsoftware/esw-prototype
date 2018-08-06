package tmt.assembly.api

import tmt.sequencer.models.{CommandResponseWeb, ControlCommandWeb}

import scala.concurrent.Future

trait AssemblyCommandWeb {
  def submit(controlCommandWeb: ControlCommandWeb): Future[CommandResponseWeb]
}

object AssemblyCommandWeb {
  val Submit = "Submit"
}
