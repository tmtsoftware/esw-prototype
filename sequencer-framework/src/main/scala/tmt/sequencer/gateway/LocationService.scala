package tmt.sequencer.gateway

import tmt.sequencer.models.{Command, CommandResponse}

import scala.concurrent.{ExecutionContext, Future}

class LocationService {

  def commandService(name: String): CommandService = CommandService(AkkaLocation(name))

  def sequenceProcessorUri(sequencerId: String, observingMode: String): String = {
    val port = (sequencerId, observingMode) match {
      case ("iris", "darknight")  => 8000
      case ("iris", "clearskies") => 8001
      case ("tcs", "darknight")   => 7000
      case ("tcs", "clearskies")  => 7001
      case _                      => throw new RuntimeException(s"can not locate sequencer=$sequencerId and observingMode=$observingMode")
    }
    s"http://0.0.0.0:$port"
  }

  def gitAddress(): (String, Int) = ("0.0.0.0", 4000)
}

case class AkkaLocation(name: String)

case class CommandService(assemblyLoc: AkkaLocation) {
  def submit(command: Command)(implicit ec: ExecutionContext): Future[CommandResponse] = Future {
    Thread.sleep(2000)
    CommandResponse.Success(command.id, s"Result submit: [${assemblyLoc.name}] - $command")
  }

  def submitAndSubscribe(command: Command)(implicit ec: ExecutionContext): Future[CommandResponse] = Future {
    CommandResponse.Success(command.id, s"Result submit and subscribe: [${assemblyLoc.name}] - $command")
  }
}
