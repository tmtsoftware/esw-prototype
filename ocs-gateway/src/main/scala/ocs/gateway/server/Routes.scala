package ocs.gateway.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.{util, Done}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.params.commands.{ControlCommand, SequenceCommand}
import csw.params.core.models.Id
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import ocs.api._
import ocs.api.codecs.SequencerJsonSupport
import ocs.api.models._
import ocs.factory.{ComponentFactory, LocationServiceWrapper}
import ocs.gateway.assembly.{AssemblyService, PositionTracker}
import ocs.gateway.{EventMonitor, SequencerMonitor}
import play.api.libs.json.Json

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class Routes(
    locationService: LocationServiceWrapper,
    componentFactory: ComponentFactory,
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
          path("subscribe" / Segment) { subsystem =>
            parameters(("component".?, "event".?)) { (component, event) =>
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
        val sequenceFeeder = componentFactory.sequenceFeeder(sequencerId, observingMode)
        val sequenceEditor = componentFactory.sequenceEditor(sequencerId, observingMode)

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
        pathPrefix(SequenceFeeder.ApiName) {
          post {
            path(SequenceFeeder.Feed) {
              entity(as[Sequence]) { commandList =>
                onSuccess(sequenceEditor.flatMap(_.isAvailable)) { isAvailable =>
                  validate(isAvailable, "Previous sequence is still running, cannot feed another sequence") {
                    sequenceFeeder.map(_.feed(commandList))
                    complete(HttpResponse(StatusCodes.Accepted, entity = "Done"))
                  }
                }
              }
            }
          }
        } ~
        pathPrefix(SequenceEditor.ApiName) {
          get {
            path(SequenceEditor.Sequence) {
              val eventualSequence: Future[StepList] = sequenceEditor.flatMap(_.sequence)
              complete(eventualSequence)
            }
          } ~
          post {
            path(SequenceEditor.AddAll) {
              entity(as[List[SequenceCommand]]) { commands =>
                complete(sequenceEditor.flatMap(_.addAll(commands).map(_ => Done)))
              }
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
              entity(as[CommandsWithTargetId]) { commandsWithTargetId =>
                import commandsWithTargetId._
                complete(sequenceEditor.flatMap(_.replace(targetId, commands).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.InsertAfter) {
              entity(as[CommandsWithTargetId]) { commandsWithTargetId =>
                import commandsWithTargetId._
                complete(sequenceEditor.flatMap(_.insertAfter(targetId, commands).map(_ => Done)))
              }
            } ~
            path(SequenceEditor.Shutdown) {
              complete(sequenceEditor.flatMap(_.shutdown().map(_ => Done)))
            }
          }
        }
      } ~
      pathPrefix("assembly" / Segment) { assemblyName =>
        val commandService = componentFactory.assembly(assemblyName)
        get {
          path("track") {
            complete(positionTracker.track(assemblyName).map(x => Json.toJson(x).toString()).map(x => ServerSentEvent(x)))
          }
        } ~
        post {
          path(AssemblyFeeder.SubmitAndSubscribe) {
            entity(as[ControlCommand]) { command =>
              implicit val timeout: Timeout = util.Timeout(10.seconds)
              complete(commandService.flatMap(_.complete(command)))
            }
          } ~
          path(AssemblyFeeder.Submit) {
            entity(as[ControlCommand]) { command =>
              implicit val timeout: Timeout = util.Timeout(10.seconds)
              complete(commandService.flatMap(_.submit(command)))
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
