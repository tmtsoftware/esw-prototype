package tmt.ocs.api

import csw.messages.commands.{CommandResponse, ControlCommand}

import scala.concurrent.Future

trait AssemblyFeeder {
  def submit(controlCommand: ControlCommand): Future[CommandResponse]
}

object AssemblyFeeder {
  val Submit = "submit"
}
