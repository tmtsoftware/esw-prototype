package ocs.framework.wrapper

import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{typed, ActorSystem}
import akka.util
import akka.util.Timeout
import csw.command.scaladsl.CommandService
import csw.location.api.models.ComponentType
import csw.params.commands.{CommandResponse, ControlCommand}

import scala.async.Async.{async, await}
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class CommandServiceWrapper(locationService: LocationServiceWrapper)(implicit system: ActorSystem) {

  implicit val typedSystem: typed.ActorSystem[Nothing] = system.toTyped

  def submit(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).submit(command))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

  def submitAndSubscribe(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).submitAndSubscribe(command))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

  def oneway(assemblyName: String, command: ControlCommand): Future[CommandResponse] = {
    locationService.resolve(assemblyName, ComponentType.Assembly) { akkaLocation =>
      async {
        implicit val timeout: Timeout = util.Timeout(10.seconds)
        val response                  = await(new CommandService(akkaLocation).oneway(command))
        println(s"Response - $response")
        response
      }(system.dispatcher)
    }
  }

}
