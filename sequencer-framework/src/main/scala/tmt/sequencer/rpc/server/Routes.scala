package tmt.sequencer.rpc.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.SequenceCommand
import csw.messages.params.formats.JsonSupport
import csw.messages.params.models.Id
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.models.CommandList
import JsonSupport._
import akka.Done
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.concurrent.ExecutionContext

class Routes(sequenceFeeder: SequenceFeeder, sequenceEditor: SequenceEditor)(implicit ec: ExecutionContext)
    extends PlayJsonSupport {

  val route: Route = cors() {
    post {
      pathPrefix(SequenceFeeder.ApiName) {
        path(SequenceFeeder.Feed) {
          entity(as[CommandList]) { commandList =>
            complete(sequenceFeeder.feed(commandList))
          }
        }
      } ~
      pathPrefix(SequenceEditor.ApiName) {
        path(SequenceEditor.AddAll) {
          entity(as[List[SequenceCommand]]) { commands =>
            complete(sequenceEditor.addAll(commands).map(_ => Done))
          }
        } ~
        path(SequenceEditor.Pause) {
          complete(sequenceEditor.pause().map(_ => Done))
        } ~
        path(SequenceEditor.Resume) {
          complete(sequenceEditor.resume().map(_ => Done))
        } ~
        path(SequenceEditor.Reset) {
          complete(sequenceEditor.reset().map(_ => Done))
        } ~
        path(SequenceEditor.Delete) {
          entity(as[List[Id]]) { ids =>
            complete(sequenceEditor.delete(ids).map(_ => Done))
          }
        } ~
        path(SequenceEditor.AddBreakpoints) {
          entity(as[List[Id]]) { ids =>
            complete(sequenceEditor.addBreakpoints(ids).map(_ => Done))
          }
        } ~
        path(SequenceEditor.RemoveBreakpoints) {
          entity(as[List[Id]]) { ids =>
            complete(sequenceEditor.removeBreakpoints(ids).map(_ => Done))
          }
        } ~
        path(SequenceEditor.Prepend) {
          entity(as[List[SequenceCommand]]) { commands =>
            complete(sequenceEditor.prepend(commands).map(_ => Done))
          }
        } ~
        path(SequenceEditor.Replace) {
          entity(as[(Id, List[SequenceCommand])]) {
            case (id, commands) =>
              complete(sequenceEditor.replace(id, commands).map(_ => Done))
          }
        } ~
        path(SequenceEditor.Shutdown) {
          complete(sequenceEditor.shutdown().map(_ => Done))
        }
      }
    }
  }
}
