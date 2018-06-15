package tmt.sequencer

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.adapter.UntypedActorSystemOps
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import ammonite.ops.{Path, RelPath}
import csw.services.location.commons.ClusterSettings
import csw.services.location.scaladsl.{LocationService, LocationServiceFactory}
import tmt.sequencer.api.{SequenceEditor, SequenceFeeder}
import tmt.sequencer.dsl.{CswServices, Script}
import tmt.sequencer.messages.{SequencerMsg, SupervisorMsg}
import tmt.sequencer.rpc.server._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationDouble

class Wiring(sequencerId: String, observingMode: String, port: Option[Int]) {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)
  lazy val clusterSettings           = ClusterSettings()

  lazy implicit val system: ActorSystem                = clusterSettings.system
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val coordinatedShutdown: CoordinatedShutdown = CoordinatedShutdown(system)

  lazy val scriptConfigs = new ScriptConfigs(system)
  lazy val path: Path    = ammonite.ops.pwd / RelPath(scriptConfigs.scriptFactoryPath)

  lazy val sequencerRef: ActorRef[SequencerMsg] = system.spawn(SequencerBehaviour.behavior, "sequencer")
  lazy val sequencer                            = new Sequencer(sequencerRef, system)

  lazy val locationService: LocationService = LocationServiceFactory.withSystem(system)

  lazy val engine         = new Engine
  lazy val cswServices    = new CswServices(sequencer, engine, locationService, sequencerId, observingMode)
  lazy val script: Script = ScriptImports.load(path).get(cswServices)

  lazy val supervisorRef: ActorRef[SupervisorMsg] = system.spawn(SupervisorBehavior.behavior(sequencerRef, script), "supervisor")

  lazy val sequenceEditor: SequenceEditor = new SequenceEditorImpl(supervisorRef, script)
  lazy val sequenceFeeder: SequenceFeeder = new SequenceFeederImpl(supervisorRef)
  lazy val routes                         = new Routes(sequenceFeeder, sequenceEditor)
  lazy val rpcConfigs                     = new RpcConfigs(port)
  lazy val rpcServer                      = new RpcServer(rpcConfigs, routes)

  lazy val remoteRepl = new RemoteRepl(cswServices, sequencer, supervisorRef, sequenceFeeder, sequenceEditor, rpcConfigs)
}
