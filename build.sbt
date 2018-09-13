import sbt.Keys.{libraryDependencies, resolvers}
import sbtcrossproject.CrossType
import sbtcrossproject.CrossPlugin.autoImport.crossProject

inThisBuild(
  List(
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
  )
)

lazy val `esw-prototype` = project
  .in(file("."))
  .aggregate(
    `ocs-api-js`,
    `ocs-api-jvm`,
    `sequencer-macros`,
    `ocs-framework`,
    `ocs-react4s-app`,
    `ocs-gateway`
  )

lazy val `ocs-api` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`play-json`.value,
      Libs.`play-json-derived-codecs`.value,
      Csw.`csw-params`.value,
      Enumeratum.`enumeratum`.value,
      SharedLibs.scalaTest.value % Test,
    )
  )

lazy val `ocs-api-js` = `ocs-api`.js
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scalajs-dom`.value,
    )
  )
lazy val `ocs-api-jvm` = `ocs-api`.jvm
  .settings(
    libraryDependencies ++= Seq(
      Akka.`akka-typed`,
      Csw.`csw-messages`
    )
  )

lazy val `ocs-react4s-app` = project
  .enablePlugins(ScalaJSBundlerPlugin)
  .dependsOn(`ocs-api-js`)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    npmDependencies in Compile ++= Seq(
      "react"     -> "16.4.1",
      "react-dom" -> "16.4.1"
    ),
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    libraryDependencies ++= Seq(
      SharedLibs.scalaTest.value % Test,
      React4s.`react4s`.value,
      React4s.`router4s`.value,
      Libs.`scala-async`.value
    ),
    version in webpack := "4.8.1",
    version in startWebpackDevServer := "3.1.4",
//    webpackConfigFile in fastOptJS := Some(baseDirectory.value / "my.custom.webpack.config.js"),
    webpackResources := webpackResources.value +++ PathFinder(Seq(baseDirectory.value / "index.html")) ** "*.*",
    webpackDevServerExtraArgs in fastOptJS ++= Seq(
      "--content-base",
      baseDirectory.value.getAbsolutePath
    )
  )

lazy val `sequencer-macros` = project
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scala-async`.value,
      Libs.`scala-reflect`,
    )
  )

lazy val `ocs-gateway` = project
  .enablePlugins(DeployApp)
  .dependsOn(`ocs-api-jvm`)
  .settings(
    libraryDependencies ++= Seq(
      Csw.`csw-messages`,
      Akka.`akka-http`,
      Akka.`akka-stream`,
      Csw.`csw-event-client`,
      Csw.`romaine`,
      Libs.`akka-http-cors`,
      Csw.`csw-location`,
      Csw.`csw-command`
    )
  )

lazy val `ocs-framework` = project
  .enablePlugins(JavaAppPackaging)
  .dependsOn(`sequencer-macros`, `ocs-api-jvm`)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scala-reflect`,
      Libs.`akka-http-cors`,
      Libs.`scala-compiler`,
      Akka.`akka-stream`,
      Akka.`akka-typed`,
      Akka.`akka-typed-testkit` % Test,
      Ammonite.`ammonite`,
      Ammonite.`ammonite-sshd`,
      Libs.`scala-async`.value,
      Libs.`akka-http-cors`,
      Akka.`akka-http`,
      Libs.`play-json`.value,
      Akka.`akka-http-play-json`,
      Csw.`csw-location`,
      Csw.`csw-command`,
      Csw.`csw-event-client`,
      Csw.`romaine`,
      SharedLibs.scalaTest.value % Test
    )
  )

lazy val `sequencer-scripts-test` = project
  .dependsOn(`ocs-framework`)
