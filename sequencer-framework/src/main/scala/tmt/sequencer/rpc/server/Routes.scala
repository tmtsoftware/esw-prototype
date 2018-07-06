package tmt.sequencer.rpc.server

import akka.Done
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.params.models.Id
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import tmt.sequencer.api.{SequenceEditorWeb, SequenceFeederWeb}
import tmt.sequencer.models.{CommandListWeb, SequenceCommandWeb, UpickleRWSupport}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Routes(sequenceFeeder: SequenceFeederWeb, sequenceEditor: SequenceEditorWeb)(implicit ec: ExecutionContext)
    extends UpickleSupport
    with UpickleRWSupport {

  val route: Route = cors() {
    post {
      pathPrefix(SequenceFeederWeb.ApiName) {
        path(SequenceFeederWeb.Feed) {
          withRequestTimeout(40.seconds) {
            entity(as[CommandListWeb]) { commandListWeb =>
              complete(sequenceFeeder.feed(commandListWeb))
            }
          }
        }
      } ~
      pathPrefix(SequenceEditorWeb.ApiName) {
        path(SequenceEditorWeb.AddAll) {
          entity(as[List[SequenceCommandWeb]]) { commands =>
            complete(sequenceEditor.addAll(commands).map(_ => Done))
          }
        } ~
        path(SequenceEditorWeb.Pause) {
          complete(sequenceEditor.pause().map(_ => Done))
        } ~
        path(SequenceEditorWeb.Resume) {
          complete(sequenceEditor.resume().map(_ => Done))
        } ~
        path(SequenceEditorWeb.Reset) {
          complete(sequenceEditor.reset().map(_ => Done))
        } ~
        path(SequenceEditorWeb.Delete) {
          entity(as[List[String]]) { ids =>
            complete(sequenceEditor.delete(ids).map(_ => Done))
          }
        } ~
        path(SequenceEditorWeb.AddBreakpoints) {
          entity(as[List[String]]) { ids =>
            complete(sequenceEditor.addBreakpoints(ids).map(_ => Done))
          }
        } ~
        path(SequenceEditorWeb.RemoveBreakpoints) {
          entity(as[List[String]]) { ids =>
            complete(sequenceEditor.removeBreakpoints(ids).map(_ => Done))
          }
        } ~
        path(SequenceEditorWeb.Prepend) {
          entity(as[List[SequenceCommandWeb]]) { commands =>
            complete(sequenceEditor.prepend(commands).map(_ => Done))
          }
        } ~
        path(SequenceEditorWeb.Replace) {
          entity(as[(String, List[SequenceCommandWeb])]) {
            case (id, commands) =>
              complete(sequenceEditor.replace(id, commands).map(_ => Done))
          }
        } ~
        path(SequenceEditorWeb.Shutdown) {
          complete(sequenceEditor.shutdown().map(_ => Done))
        }
      }
    }
  }
}
