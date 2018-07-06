package tmt.sequencer.rpc.server

import akka.Done
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.params.models.Id
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.models.{CommandList, SequenceCommandWeb, UpickleRWSupport}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Routes(sequenceFeeder: SequenceFeeder, sequenceEditor: SequenceEditor)(implicit ec: ExecutionContext)
    extends UpickleSupport
    with UpickleRWSupport {

  val route: Route = cors() {
    post {
//      pathPrefix(SequenceFeeder.ApiName) {
//        path(SequenceFeeder.Feed) {
//          withRequestTimeout(40.seconds) {
//            entity(as[CommandList]) { commandList =>
//              complete(sequenceFeeder.feed(commandList))
//            }
//          }
//        }
//      } ~
      pathPrefix(SequenceEditor.ApiName) {
        path(SequenceEditor.AddAll) {
          entity(as[List[SequenceCommandWeb]]) { commands =>
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
          entity(as[List[SequenceCommandWeb]]) { commands =>
            complete(sequenceEditor.prepend(commands).map(_ => Done))
          }
        } ~
        path(SequenceEditor.Replace) {
          entity(as[(Id, List[SequenceCommandWeb])]) {
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
