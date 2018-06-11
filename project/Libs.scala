import sbt._
import Def.{setting => dep}
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Libs {
  val ScalaVersion = "2.12.6"

  val `scala-reflect`      = "org.scala-lang"         % "scala-reflect"       % ScalaVersion
  val `scala-compiler`     = "org.scala-lang"         % "scala-compiler"      % ScalaVersion
  val `scala-java8-compat` = "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0" //BSD 3-clause "New" or "Revised" License
  val `jgit`               = "org.eclipse.jgit"       % "org.eclipse.jgit"    % "4.11.0.201803080745-r"
  val `scala-async`        = "org.scala-lang.modules" %% "scala-async"        % "0.9.7"
  val `enumeratum`         = "com.beachape"           %% "enumeratum"         % "1.5.13"
  val `akka-http-cors`     = "ch.megard"              %% "akka-http-cors"     % "0.3.0"
  val `scalajs-library`    = "org.scala-js"           %% "scalajs-library"    % "0.6.22"
}

object Ammonite {
  val Version = "1.1.2"

  val `ammonite`      = "com.lihaoyi" % "ammonite"      % Version cross CrossVersion.full
  val `ammonite-sshd` = "com.lihaoyi" % "ammonite-sshd" % Version cross CrossVersion.full
}

object Akka {
  val Version = "2.5.11"

  val `akka-stream`        = "com.typesafe.akka" %% "akka-stream"        % Version
  val `akka-typed`         = "com.typesafe.akka" %% "akka-actor-typed"   % Version
  val `akka-http`          = "com.typesafe.akka" %% "akka-http"          % "10.1.1"
  val `akka-typed-testkit` = "com.typesafe.akka" %% "akka-testkit-typed" % Version
  val `akka-http-circe`    = "de.heikoseeberger" %% "akka-http-circe"    % "1.20.1" //Apache 2.0
}

object SharedLibs {
  val `scalaTest` = dep("org.scalatest" %%% "scalatest" % "3.0.5")
}

object Circe {
  val Version = "0.9.3"

  val `circe-core`    = dep("io.circe" %%% "circe-core"    % Version)
  val `circe-generic` = dep("io.circe" %%% "circe-generic" % Version)
  val `circe-parser`  = dep("io.circe" %%% "circe-parser"  % Version)
}
