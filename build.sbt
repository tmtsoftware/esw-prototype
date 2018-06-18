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
    `sequencer-api`,
    `sequencer-macros`,
    `sequencer-framework`,
  )

lazy val `sequencer-api` = project
  .settings(
    libraryDependencies ++= Seq(
      Csw.`csw-messages`,
      Circe.`circe-core`,
      Circe.`circe-generic`,
      Circe.`circe-parser`,
      SharedLibs.scalaTest % Test,
    )
  )

lazy val `sequencer-macros` = project
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scala-async`,
      Libs.`scala-reflect`,
    )
  )

lazy val `sequencer-framework` = project
  .enablePlugins(JavaAppPackaging)
  .dependsOn(`sequencer-macros`, `sequencer-api`)
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
      Libs.`scala-async`,
      Libs.`akka-http-cors`,
      Akka.`akka-http`,
      Akka.`akka-http-circe`,
      Circe.`circe-core`,
      Circe.`circe-generic`,
      Circe.`circe-parser`,
      Csw.`csw-location`,
      Csw.`csw-command`,
      Csw.`csw-event-client`,
      SharedLibs.scalaTest % Test
    )
  )

lazy val `location-agent-simulator` = project
  .settings(
    name := "location-agent-simulator",
    libraryDependencies ++= Seq(
      Csw.`csw-location`,
      Akka.`akka-typed`,
      SharedLibs.scalaTest % Test
    )
  )