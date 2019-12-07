import sbt._
import Def.{setting => dep}
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Libs {
  val ScalaVersion = "2.12.8"

  val `scala-reflect`      = "org.scala-lang"         % "scala-reflect"       % ScalaVersion
  val `scala-compiler`     = "org.scala-lang"         % "scala-compiler"      % ScalaVersion
  val `akka-http-cors`     = "ch.megard"              %% "akka-http-cors"     % "0.4.0"
  val `scala-java8-compat` = "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0" //BSD 3-clause "New" or "Revised" License
  val `mockito-core`       = "org.mockito"            % "mockito-core"        % "2.28.2" //MIT License
  val `reactify`           = "com.outr"               %% "reactify"           % "3.0.3"
  val `scalarx`            = "com.lihaoyi"            %% "scalarx"            % "0.4.0"

  val `scala-async`              = dep("org.scala-lang.modules" %% "scala-async"               % "0.10.0")
  val `play-json`                = dep("com.typesafe.play"      %%% "play-json"                % "2.7.3") //Apache 2.0
  val `play-functional`          = dep("com.typesafe.play"      %%% "play-functional"          % "2.7.3") //Apache 2.0
  val `play-json-derived-codecs` = dep("org.julienrf"           %%% "play-json-derived-codecs" % "5.0.0")
  val `monix`                    = dep("io.monix"               %%% "monix"                    % "3.0.0-RC1")
  val `scalajs-dom`              = dep("org.scala-js"           %%% "scalajs-dom"              % "0.9.7")
  val airstream                  = dep("com.raquo"              %%% "airstream"                % "0.7")
  val laminar                    = dep("com.raquo"              %%% "laminar"                  % "0.7.2")
}

object Enumeratum {
  val version      = "1.5.13"
  val `enumeratum` = dep("com.beachape" %%% "enumeratum" % version) //MIT License
}

object Csw {
  private val Org     = "com.github.tmtsoftware.csw"
  private val Version = "8c7a74b" //change this to 0.1-SNAPSHOT to test with local csw changes (after publishLocal)

  val `csw-location-client` = Org %% "csw-location-client" % Version
  val `csw-location-api`    = Org %% "csw-location-api"    % Version
  val `csw-command-client`  = Org %% "csw-command-client"  % Version
  val `csw-time-scheduler`  = Org %% "csw-time-scheduler"  % Version

  val `csw-params` = dep(Org %%% "csw-params" % Version)

  val `csw-event-client` = Org %% "csw-event-client" % Version
  val `romaine`          = Org %% "romaine"          % Version
}

object Ammonite {
  val Version = "1.6.7"

  val `ammonite`      = "com.lihaoyi" % "ammonite"      % Version cross CrossVersion.full
  val `ammonite-sshd` = "com.lihaoyi" % "ammonite-sshd" % Version cross CrossVersion.full
}

object Akka {
  val Version = "2.5.23"

  val `akka-actor`          = "com.typesafe.akka" %% "akka-actor"               % Version
  val `akka-stream`         = "com.typesafe.akka" %% "akka-stream"              % Version
  val `akka-typed`          = "com.typesafe.akka" %% "akka-actor-typed"         % Version
  val `akka-http`           = "com.typesafe.akka" %% "akka-http"                % "10.1.8"
  val `akka-typed-testkit`  = "com.typesafe.akka" %% "akka-actor-testkit-typed" % Version
  val `akka-http-play-json` = "de.heikoseeberger" %% "akka-http-play-json"      % "1.26.0" //Apache 2.0
}

object SharedLibs {
  val `scalaTest` = dep("org.scalatest" %%% "scalatest" % "3.0.8")
}

object React4s {
  val `react4s` = dep("com.github.ahnfelt" %%% "react4s" % "0.9.26-SNAPSHOT")
}

object Sparse {
  private val Version = "0.2.0"
  val `trail`         = dep("tech.sparse" %%% "trail" % Version)
}
