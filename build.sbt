import com.typesafe.sbt.web.Import.WebKeys.webJarsNodeModulesDirectory
import sbtcrossproject.{CrossType, crossProject}

inThisBuild(List(
  organization := "org.tmt",
  scalaVersion := "2.12.6",
  version := "0.1.0-SNAPSHOT",
  resolvers += "jitpack" at "https://jitpack.io",
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    //"-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Xfuture",
    //      "-Xprint:typer"
  )
))

lazy val `esw-prototype` = project
  .in(file("."))
  .aggregate(
    `sequencer-api-js`,
    `sequencer-api-jvm`,
    `sequencer-macros`,
    `sequencer-framework`,
  )

lazy val `sequencer-api` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(
    libraryDependencies ++= Seq(
      Circe.`circe-core`.value,
      Circe.`circe-generic`.value,
      Circe.`circe-parser`.value,
      SharedLibs.scalaTest.value % Test,
    )
  )

lazy val `sequencer-api-js` = `sequencer-api`.js
lazy val `sequencer-api-jvm` = `sequencer-api`.jvm

lazy val `sequencer-macros` = project
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scala-async`,
      Libs.`scala-reflect`,
    )
  )

lazy val `sequencer-framework` = project
  .enablePlugins(JavaAppPackaging)
  .dependsOn(`sequencer-macros`, `sequencer-api-jvm`)
  .settings(
    name := "sequencer-framework",
    libraryDependencies ++= Seq(
      Libs.`scala-reflect`,
      Libs.`akka-http-cors`,
      Libs.`scala-compiler`,
      Akka.`akka-stream`,
      Akka.`akka-typed`,
      Akka.`akka-typed-testkit`,
      Ammonite.`ammonite`,
      Ammonite.`ammonite-sshd`,
      Libs.`jgit`,
      Libs.`enumeratum`,
      Libs.`scala-async`,
      Libs.`akka-http-cors`,
      Akka.`akka-http`,
      Akka.`akka-http-circe`,
      Circe.`circe-core`.value,
      Circe.`circe-generic`.value,
      Circe.`circe-parser`.value,
      Csw.`csw-location`,
      SharedLibs.scalaTest.value % Test
    )
  )