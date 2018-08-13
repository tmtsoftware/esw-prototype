package tmt.sequencer.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.Timeout
import akka.{util, Done, NotUsed}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.{ControlCommand, SequenceCommand}
import csw.messages.events.{Event, EventKey}
import csw.messages.params.models.Id
import csw.services.event.api.scaladsl.EventService
import csw.services.location.internal.UpickleFormats
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import tmt.assembly.api.AssemblyCommandWeb
import tmt.sequencer.LocationServiceGateway
import tmt.sequencer.api.{SequenceEditorWeb, SequenceFeederWeb, SequenceResultsWeb}
import tmt.sequencer.models._
import tmt.sequencer.util.SequencerUtil

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class Routes(
    locationService: LocationServiceGateway,
    eventService: EventService
)(implicit ec: ExecutionContext, val actorSystem: ActorSystem)
    extends UpickleSupport
    with UpickleRWSupport
    with UpickleFormats {

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

  def stream(sequencerId: String, observingMode: String): Source[Event, NotUsed] =
    Source
      .fromFuture(eventService.defaultSubscriber)
      .flatMapConcat(_.subscribe(Set(EventKey(s"$sequencerId-$observingMode.result"))))

  val route: Route = cors() {
    SequencerExceptionHandler.route {
      {
        get {
          pathPrefix("locations") {
            path("sequencers") {
              val eventualLocations = locationService.listSequencers()
              val eventualSequencerPaths =
                eventualLocations.map(_.map(location => SequencerUtil.parseSequencerLocation(location.connection.name)))
              complete(eventualSequencerPaths)
            } ~
            path("assemblies") {
              val eventualLocations = locationService.listAssemblies()
              val eventualAssemblyPaths =
                eventualLocations.map(_.map(location => SequencerUtil.parseAssemblyLocation(location.connection.name)))
              complete(eventualAssemblyPaths)
            }
          }
        } ~
        pathPrefix("sequencer" / Segment / Segment) { (sequencerId, observingMode) =>
          get {
            path(SequenceResultsWeb.results) {
              complete {
                stream(sequencerId, observingMode)
                  .map(event => ServerSentEvent(event.paramSet.toString()))
                  .keepAlive(10.second, () => ServerSentEvent.heartbeat)
              }
            }
          } ~
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
                val eventualSequence: Future[Sequence] = sequenceEditor.flatMap(_.sequence)
                complete(eventualSequence)
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
          }
        } ~
        pathPrefix("assembly" / Segment) { assemblyName =>
          val commandService = locationService.commandServiceFor(assemblyName)
          post {
            path(AssemblyCommandWeb.Submit) {
              entity(as[ControlCommand]) { command =>
                implicit val timeout: Timeout = util.Timeout(10.seconds)
                complete(commandService.flatMap(_.submit(command)))
              }
            }
          }
        }
      }
    }
  }
}
