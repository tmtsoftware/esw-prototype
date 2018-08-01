package tmt.sequencer.rpc.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.{Done, NotUsed}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.SequenceCommand
import csw.messages.events.EventKey
import csw.messages.location.ComponentType
import csw.messages.params.models.Id
import csw.services.event.api.scaladsl.{EventService, EventSubscriber}
import csw.services.location.internal.UpickleFormats
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import tmt.sequencer.LocationServiceGateway
import tmt.sequencer.api.{SequenceEditorWeb, SequenceFeederWeb, SequenceResultsWeb}
import tmt.sequencer.models._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class Routes(
    locationService: LocationServiceGateway,
    eventService: EventService
)(implicit ec: ExecutionContext, val actorSystem: ActorSystem)
    extends UpickleSupport
    with UpickleRWSupport
    with UpickleFormats {

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

  val stream: Source[EventSubscriber, NotUsed] = {
    Source
      .fromFuture(eventService.defaultSubscriber)
  }

  val route: Route = cors() {
    SequencerExceptionHandler.route {
      get {
        pathSingleSlash {
          complete(locationService.list(ComponentType.Sequencer))
        }
      } ~
      pathPrefix("sequencer" / Segment / Segment) { (sequencerId, observingMode) =>
        {
          post {
            val sequenceFeeder = locationService.sequenceFeeder(sequencerId, observingMode)
            val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)

            pathPrefix(SequenceFeederWeb.ApiName) {
              path(SequenceFeederWeb.Feed) {
                entity(as[CommandList]) { commandList =>
                  onSuccess(sequenceEditor.flatMap(_.isAvailable)) { isAvailable =>
                    validate(isAvailable, "Previous sequence is still running, cannot feed another sequence") {
                      sequenceFeeder.map(_.feed(commandList))
                      complete(HttpResponse(StatusCodes.Accepted, entity = "Done"))
                    }
                  }
                }
              }
            } ~
            pathPrefix(SequenceEditorWeb.ApiName) {
              path(SequenceEditorWeb.AddAll) {
                entity(as[List[SequenceCommand]]) { commands =>
                  complete(sequenceEditor.flatMap(_.addAll(commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Sequence) {
                complete(sequenceEditor.flatMap(_.sequence))
              } ~
              path(SequenceEditorWeb.Pause) {
                complete(sequenceEditor.flatMap(_.pause().map(_ => Done)))
              } ~
              path(SequenceEditorWeb.Resume) {
                complete(sequenceEditor.flatMap(_.resume().map(_ => Done)))
              } ~
              path(SequenceEditorWeb.Reset) {
                complete(sequenceEditor.flatMap(_.reset().map(_ => Done)))
              } ~
              path(SequenceEditorWeb.Delete) {
                entity(as[List[Id]]) { ids =>
                  complete(sequenceEditor.flatMap(_.delete(ids).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.AddBreakpoints) {
                entity(as[List[Id]]) { ids =>
                  complete(sequenceEditor.flatMap(_.addBreakpoints(ids).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.RemoveBreakpoints) {
                entity(as[List[Id]]) { ids =>
                  complete(sequenceEditor.flatMap(_.removeBreakpoints(ids).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Prepend) {
                entity(as[List[SequenceCommand]]) { commands =>
                  complete(sequenceEditor.flatMap(_.prepend(commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Replace) {
                entity(as[(Id, List[SequenceCommand])]) {
                  case (id, commands) =>
                    complete(sequenceEditor.flatMap(_.replace(id, commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.InsertAfter) {
                entity(as[(Id, List[SequenceCommand])]) {
                  case (id, commands) =>
                    complete(sequenceEditor.flatMap(_.insertAfter(id, commands).map(_ => Done)))
                }
              } ~
              path(SequenceEditorWeb.Shutdown) {
                complete(sequenceEditor.flatMap(_.shutdown().map(_ => Done)))
              }
            }
          } ~
          get {
            path(SequenceResultsWeb.ApiName / SequenceResultsWeb.results) {
              complete {
                stream
                  .flatMapConcat(_.subscribe(Set(EventKey(s"$sequencerId-$observingMode.result"))))
                  .map(event => ServerSentEvent(event.paramSet.toString()))
                  .keepAlive(10.second, () => ServerSentEvent.heartbeat)
              }
            }
          }
        }
      }
    }
  }
}
