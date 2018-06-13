package tmt.sequencer.rpc.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.models.CommandList

class Routes(sequenceFeeder: SequenceFeeder, sequenceEditor: SequenceEditor) extends FailFastCirceSupport with EswCirceSupport {

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
            complete(sequenceEditor.addAll(commands))
          }
        } ~
        path(SequenceEditor.Pause) {
          entity(as[Unit]) { commands =>
            complete(sequenceEditor.pause())
          }
        } ~
        path(SequenceEditor.Resume) {
          entity(as[Unit]) { commands =>
            complete(sequenceEditor.resume())
          }
        } ~
        path(SequenceEditor.Reset) {
          entity(as[Unit]) { commands =>
            complete(sequenceEditor.reset())
          }
        } ~
        path(SequenceEditor.Delete) {
          entity(as[List[Id]]) { ids =>
            complete(sequenceEditor.delete(ids))
          }
        } ~
        path(SequenceEditor.AddBreakpoints) {
          entity(as[List[Id]]) { ids =>
            complete(sequenceEditor.addBreakpoints(ids))
          }
        } ~
        path(SequenceEditor.RemoveBreakpoints) {
          entity(as[List[Id]]) { ids =>
            complete(sequenceEditor.removeBreakpoints(ids))
          }
        } ~
        path(SequenceEditor.Prepend) {
          entity(as[List[SequenceCommand]]) { commands =>
            complete(sequenceEditor.prepend(commands))
          }
        } ~
        path(SequenceEditor.Replace) {
          entity(as[(Id, List[SequenceCommand])]) {
            case (id, commands) =>
              complete(sequenceEditor.replace(id, commands))
          }
        } ~
        path(SequenceEditor.Shutdown) {
          entity(as[Unit]) { commands =>
            complete(sequenceEditor.shutdown())
          }
        }
      }
    }
  }
}
