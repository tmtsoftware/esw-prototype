package tmt.ocs.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.{util, Done}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.messages.commands.{ControlCommand, SequenceCommand}
import csw.messages.params.models.Id
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.Json
import tmt.ocs.api.{AssemblyFeeder, SequenceEditor, SequenceFeeder, SequenceResultsWeb}
import tmt.ocs.assembly.{AssemblyService, PositionTracker}
import tmt.ocs.codecs.SequencerJsonSupport
import tmt.ocs.models._
import tmt.ocs.{EventMonitor, LocationServiceGateway, SequencerMonitor}
import tmt.sequencer.util.SequencerUtil

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class Routes(
    locationService: LocationServiceGateway,
    sequencerMonitor: SequencerMonitor,
    positionTracker: PositionTracker,
    assemblyService: AssemblyService,
    eventMonitor: EventMonitor
)(implicit ec: ExecutionContext, val actorSystem: ActorSystem)
    extends PlayJsonSupport
    with SequencerJsonSupport {

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

  val route: Route = cors() {
    SequencerExceptionHandler.route {
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
        } ~
        pathPrefix("events") {
          path("subscribe" / "subsystem" / Segment) { subsystem =>
            parameters("component".?, "event".?) { (component, event) =>
              complete {
                eventMonitor
                  .subscribe(subsystem, component, event)
                  .map(evt => ServerSentEvent(Json.stringify(Json.toJson(evt))))
                  .keepAlive(10.second, () => ServerSentEvent.heartbeat)
              }
            }
          }
        }
      } ~
      pathPrefix("sequencer" / Segment / Segment) { (sequencerId, observingMode) =>
        get {
          path(SequenceResultsWeb.results) {
            complete {
              sequencerMonitor
                .watch(sequencerId, observingMode)
                .map(event => ServerSentEvent(event))
                .keepAlive(10.second, () => ServerSentEvent.heartbeat)
            }
          }
        } ~
        post {
          val sequenceFeeder = locationService.sequenceFeeder(sequencerId, observingMode)
          val sequenceEditor = locationService.sequenceEditor(sequencerId, observingMode)
          pathPrefix(SequenceFeeder.ApiName) {
            path(SequenceFeeder.Feed) {
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
          pathPrefix(SequenceEditor.ApiName) {
            path(SequenceEditor.AddAll) {
              entity(as[List[SequenceCommand]]) { commands =>
                complete(sequenceEditor.flatMap(_.addAll(commands).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.Sequence) {
              val eventualSequence: Future[Sequence] = sequenceEditor.flatMap(_.sequence)
              complete(eventualSequence)
            } ~
            path(SequenceEditor.Pause) {
              complete(sequenceEditor.flatMap(_.pause().map(_ => Done)))
            } ~
            path(SequenceEditor.Resume) {
              complete(sequenceEditor.flatMap(_.resume().map(_ => Done)))
            } ~
            path(SequenceEditor.Reset) {
              complete(sequenceEditor.flatMap(_.reset().map(_ => Done)))
            } ~
            path(SequenceEditor.Delete) {
              entity(as[List[Id]]) { ids =>
                complete(sequenceEditor.flatMap(_.delete(ids).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.AddBreakpoints) {
              entity(as[List[Id]]) { ids =>
                complete(sequenceEditor.flatMap(_.addBreakpoints(ids).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.RemoveBreakpoints) {
              entity(as[List[Id]]) { ids =>
                complete(sequenceEditor.flatMap(_.removeBreakpoints(ids).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.Prepend) {
              entity(as[List[SequenceCommand]]) { commands =>
                complete(sequenceEditor.flatMap(_.prepend(commands).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.Replace) {
              entity(as[(Id, List[SequenceCommand])]) {
                case (id, commands) =>
                  complete(sequenceEditor.flatMap(_.replace(id, commands).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.InsertAfter) {
              entity(as[(Id, List[SequenceCommand])]) {
                case (id, commands) =>
                  complete(sequenceEditor.flatMap(_.insertAfter(id, commands).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.Shutdown) {
              complete(sequenceEditor.flatMap(_.shutdown().map(_ => Done)))
            }
          }
        }
      } ~
      pathPrefix("assembly" / Segment) { assemblyName =>
        val commandService = locationService.commandServiceFor(assemblyName)
        get {
          path("track") {
            complete(positionTracker.track(assemblyName).map(x => Json.toJson(x).toString()).map(x => ServerSentEvent(x)))
          }
        } ~
        post {
          path(AssemblyFeeder.Submit) {
            entity(as[ControlCommand]) { command =>
              implicit val timeout: Timeout = util.Timeout(10.seconds)
              complete(commandService.flatMap(_.submitAndSubscribe(command)))
            }
          } ~
          path("move") {
            entity(as[RequestComponent]) { requestComponent =>
              complete(assemblyService.oneway("Sample1Assembly", requestComponent))
            }
          }
        }
      }
    }
  }
}
