import sbt._

object Libs {
  val ScalaVersion = "2.12.6"

  val `scala-reflect`      = "org.scala-lang"         % "scala-reflect"       % ScalaVersion
  val `scala-compiler`     = "org.scala-lang"         % "scala-compiler"      % ScalaVersion
  val `scala-java8-compat` = "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0" //BSD 3-clause "New" or "Revised" License
  val `scala-async`        = "org.scala-lang.modules" %% "scala-async"        % "0.9.7"
  val `akka-http-cors`     = "ch.megard"              %% "akka-http-cors"     % "0.3.0"
}

object Csw {
  val `csw-location`     = "org.tmt" %% "csw-location"     % "0.1-SNAPSHOT"
  val `csw-command`      = "org.tmt" %% "csw-command"      % "0.1-SNAPSHOT"
  val `csw-messages`     = "org.tmt" %% "csw-messages"     % "0.1-SNAPSHOT"
  val `csw-event-api`    = "org.tmt" %% "csw-event-api"    % "0.1-SNAPSHOT"
  val `csw-event-client` = "org.tmt" %% "csw-event-client" % "0.1-SNAPSHOT"
}

object Ammonite {
  val Version = "1.1.2"

  val `ammonite`      = "com.lihaoyi" % "ammonite"      % Version cross CrossVersion.full
  val `ammonite-sshd` = "com.lihaoyi" % "ammonite-sshd" % Version cross CrossVersion.full
}

object Akka {
  val Version = "2.5.12"

  val `akka-stream`        = "com.typesafe.akka" %% "akka-stream"        % Version
  val `akka-typed`         = "com.typesafe.akka" %% "akka-actor-typed"   % Version
  val `akka-http`          = "com.typesafe.akka" %% "akka-http"          % "10.1.1"
  val `akka-typed-testkit` = "com.typesafe.akka" %% "akka-testkit-typed" % Version
  val `akka-http-circe`    = "de.heikoseeberger" %% "akka-http-circe"    % "1.20.1" //Apache 2.0
}

object SharedLibs {
  val `scalaTest` = "org.scalatest" %% "scalatest" % "3.0.5"
}

object Circe {
  val Version = "0.9.3"

  val `circe-core`    = "io.circe" %% "circe-core"    % Version
  val `circe-generic` = "io.circe" %% "circe-generic" % Version
  val `circe-parser`  = "io.circe" %% "circe-parser"  % Version
}
