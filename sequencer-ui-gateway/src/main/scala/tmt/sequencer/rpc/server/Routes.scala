package tmt.sequencer.rpc.server

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.SequenceCommand
import csw.messages.location.ComponentType
import csw.messages.params.models.Id
import csw.services.event.api.scaladsl.EventService
import csw.services.location.internal.UpickleFormats
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import tmt.sequencer.api.{SequenceEditorWeb, SequenceFeederWeb}
import tmt.sequencer.models._

import scala.concurrent.ExecutionContext

class Routes(
    locationService: LocationServiceGateway,
    eventService: EventService
)(implicit ec: ExecutionContext, val actorSystem: ActorSystem)
    extends UpickleSupport
    with UpickleRWSupport
    with UpickleFormats {

  val route: Route = cors() {
    SequencerExceptionHandler.route {
      get {
        pathSingleSlash {
          complete(locationService.list(ComponentType.Sequencer))
        }
      } ~
      post {
        pathPrefix("sequencer" / Segment / Segment) { (sequencerId, observingMode) =>
          {
            pathPrefix(SequenceFeederWeb.ApiName) {
              path(SequenceFeederWeb.Feed) {
                entity(as[CommandList]) { commandList =>
                  val sequenceFeeder = locationService.sequenceFeeder(sequencerId, observingMode)
                  val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                  onSuccess(sequenceEditor.flatMap(_.isAvailable)) { isAvailable =>
                    validate(isAvailable, "Previous sequence is still running, cannot feed another sequence") {
                      sequenceFeeder.map(_.feed(commandList))
                      complete(Done)
                    }
                  }
                }
              }
            } ~
            pathPrefix(SequenceEditorWeb.ApiName) {
              path(SequenceEditorWeb.AddAll) {
                entity(as[List[SequenceCommand]]) { commands =>
                  val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                  complete(sequenceEditor.flatMap(_.addAll(commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Sequence) {
                val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                complete(sequenceEditor.flatMap(_.sequence))
              } ~
              path(SequenceEditorWeb.Pause) {
                val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                complete(sequenceEditor.flatMap(_.pause().map(_ => Done)))
              } ~
              path(SequenceEditorWeb.Resume) {
                val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                complete(sequenceEditor.flatMap(_.resume().map(_ => Done)))
              } ~
              path(SequenceEditorWeb.Reset) {
                val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                complete(sequenceEditor.flatMap(_.reset().map(_ => Done)))
              } ~
              path(SequenceEditorWeb.Delete) {
                entity(as[List[Id]]) { ids =>
                  val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                  complete(sequenceEditor.flatMap(_.delete(ids).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.AddBreakpoints) {
                entity(as[List[Id]]) { ids =>
                  val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                  complete(sequenceEditor.flatMap(_.addBreakpoints(ids).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.RemoveBreakpoints) {
                entity(as[List[Id]]) { ids =>
                  val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                  complete(sequenceEditor.flatMap(_.removeBreakpoints(ids).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Prepend) {
                entity(as[List[SequenceCommand]]) { commands =>
                  val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                  complete(sequenceEditor.flatMap(_.prepend(commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Replace) {
                entity(as[(Id, List[SequenceCommand])]) {
                  case (id, commands) =>
                    val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                    complete(sequenceEditor.flatMap(_.replace(id, commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.InsertAfter) {
                entity(as[(Id, List[SequenceCommand])]) {
                  case (id, commands) =>
                    val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                    complete(sequenceEditor.flatMap(_.insertAfter(id, commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Shutdown) {
                val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
                complete(sequenceEditor.flatMap(_.shutdown().map(_ => Done)))
              }
            }
          }

        }
      }
    }
  }
}
