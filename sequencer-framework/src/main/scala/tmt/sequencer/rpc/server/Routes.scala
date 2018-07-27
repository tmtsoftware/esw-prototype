package tmt.sequencer.rpc.server

import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.{Done, NotUsed}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.SequenceCommand
import csw.messages.events.{Event, EventKey}
import csw.messages.params.models.Id
import csw.services.event.api.scaladsl.EventService
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import tmt.sequencer.api._
import tmt.sequencer.models._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class Routes(sequenceFeeder: SequenceFeeder,
             sequenceEditor: SequenceEditor,
             eventService: EventService,
             sequencerId: String,
             observingMode: String)(
    implicit ec: ExecutionContext
) extends UpickleSupport
    with UpickleRWSupport {

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

  val stream: Source[Event, NotUsed] = {
    Source
      .fromFuture(eventService.defaultSubscriber)
      .flatMapConcat(_.subscribe(Set(EventKey(s"$sequencerId-$observingMode.result"))))
  }

  val route: Route = cors() {
    SequencerExceptionHandler.route {
      post {
        pathPrefix(SequenceFeederWeb.ApiName) {
          path(SequenceFeederWeb.Feed) {
            entity(as[CommandList]) { commandList =>
              onSuccess(sequenceEditor.isAvailable) { isAvailable =>
                validate(isAvailable, "Previous sequence is still running, cannot feed another sequence") {
                  sequenceFeeder.feed(commandList)
                  complete(Done)
                }
              }
            }
          }
        } ~
        pathPrefix(SequenceEditorWeb.ApiName) {
          path(SequenceEditorWeb.AddAll) {
            entity(as[List[SequenceCommand]]) { commands =>
              complete(sequenceEditor.addAll(commands).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.Sequence) {
            complete(sequenceEditor.sequence)
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
            entity(as[List[Id]]) { ids =>
              complete(sequenceEditor.delete(ids).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.AddBreakpoints) {
            entity(as[List[Id]]) { ids =>
              complete(sequenceEditor.addBreakpoints(ids).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.RemoveBreakpoints) {
            entity(as[List[Id]]) { ids =>
              complete(sequenceEditor.removeBreakpoints(ids).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.Prepend) {
            entity(as[List[SequenceCommand]]) { commands =>
              complete(sequenceEditor.prepend(commands).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.Replace) {
            entity(as[(Id, List[SequenceCommand])]) {
              case (id, commands) =>
                complete(sequenceEditor.replace(id, commands).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.InsertAfter) {
            entity(as[(Id, List[SequenceCommand])]) {
              case (id, commands) =>
                complete(sequenceEditor.insertAfter(id, commands).map(_ => Done))
            }
          } ~
          path(SequenceEditorWeb.Shutdown) {
            complete(sequenceEditor.shutdown().map(_ => Done))
          }
        }
      } ~
      get {
        path(SequenceResultsWeb.ApiName / SequenceResultsWeb.results) {
          complete {
            stream
              .map(event => ServerSentEvent(event.paramSet.toString()))
              .keepAlive(10.second, () => ServerSentEvent.heartbeat)
          }
        }
      } ~
      get {
        pathSingleSlash {
          getFromResource("web/index.html")
        } ~
        path("sequencer-ui-app-fastopt-bundle.js")(getFromResource("sequencer-ui-app-fastopt-bundle.js"))
      }

    }
  }
}
