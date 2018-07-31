import sbt.Keys.{libraryDependencies, resolvers}
import sbtcrossproject.{CrossType, crossProject}
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.npmDependencies

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
    `sequencer-ui-app`
  )

lazy val `sequencer-api` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`play-json`.value,
      Libs.`upickle`.value,
      Enumeratum.`enumeratum`.value,
      SharedLibs.scalaTest.value % Test,
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      Libs.`scalajs-dom`.value,
    )
  )

lazy val `sequencer-api-js` = `sequencer-api`.js
lazy val `sequencer-api-jvm` = `sequencer-api`.jvm

lazy val `sequencer-ui-app` = project
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .dependsOn(`sequencer-api-js`)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    npmDependencies in Compile ++= Seq(
      "react" -> "16.4.1",
      "react-dom" -> "16.4.1"
    ),
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    libraryDependencies ++= Seq(
      SharedLibs.scalaTest.value % Test,
      React4s.`react4s`.value
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
  .dependsOn(`sequencer-macros`, `sequencer-api-jvm`)
  .settings(
    name := "sequencer-framework",
    libraryDependencies ++= Seq(
      Libs.`scala-reflect`,
      Libs.`akka-http-cors`,
      Libs.`scala-compiler`,
      Libs.`ujson-play`,
      Akka.`akka-stream`,
      Akka.`akka-typed`,
      Akka.`akka-typed-testkit` % Test,
      Ammonite.`ammonite`,
      Ammonite.`ammonite-sshd`,
      Libs.`scala-async`,
      Libs.`akka-http-cors`,
      Akka.`akka-http`,
      Libs.`play-json`.value,
      Akka.`akka-http-play-json`,
      Csw.`csw-location`,
      Csw.`csw-command`,
      Csw.`csw-event-client`,
      SharedLibs.scalaTest.value % Test
    ),
    resourceGenerators in Compile += Def.task {
      Seq((`sequencer-ui-app` / Compile / fastOptJS / webpack).value.head.data)
    }.taskValue,
    watchSources ++= (watchSources in `sequencer-ui-app`).value
  )

lazy val `sequencer-scripts-test` = project
  .dependsOn(`sequencer-framework`)
  .settings(name := "sequencer-scripts-test")