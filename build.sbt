import sbt.Keys.{libraryDependencies, resolvers}
import sbtcrossproject.CrossType
import sbtcrossproject.CrossPlugin.autoImport.crossProject

inThisBuild(
  List(
    organization := "com.github.tmtsoftware.esw-prototype",
    scalaVersion := Libs.ScalaVersion,
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
    `ocs-testkit`,
    `react4s-facade`,
    `ocs-react4s-app`,
    `ocs-gateway`,
    `ocs-client`,
    `ui-spikes`
  )

lazy val `ocs-api` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`play-json`.value,
      Libs.`play-functional`.value,
      Libs.`play-json-derived-codecs`.value,
      Csw.`csw-params`.value,
      Enumeratum.`enumeratum`.value,
      SharedLibs.scalaTest.value % Test,
      Csw.`csw-location-api`
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
      Akka.`akka-actor`
    )
  )

lazy val `react4s-facade` = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      SharedLibs.scalaTest.value % Test,
      React4s.`react4s`.value,
    )
  )

lazy val `ocs-react4s-app` = project
  .enablePlugins(ScalaJSBundlerPlugin)
  .dependsOn(`ocs-api-js`, `react4s-facade`)
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
      Sparse.`trail`.value,
      Libs.`scala-async`.value
    ),
    version in webpack := "4.8.1",
    version in startWebpackDevServer := "3.1.4",
    webpackResources := webpackResources.value +++ PathFinder(Seq(baseDirectory.value / "index.html")) ** "*.*",
    webpackDevServerExtraArgs in fastOptJS ++= Seq(
      "--content-base",
      baseDirectory.value.getAbsolutePath
    )
  )

lazy val `sequencer-macros` = project
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scala-reflect`,
    )
  )

lazy val `ocs-client` = project
  .enablePlugins(DeployApp)
  .configs(IntegrationTest)
  .dependsOn(`ocs-api-jvm`)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      Csw.`csw-location-client`,
      Csw.`csw-command-client`,
      Csw.`csw-event-client`,
      Csw.`romaine`,
      Ammonite.`ammonite`,
      SharedLibs.`scalaTest`.value % "it,test",
    )
  )

lazy val `ocs-gateway` = project
  .enablePlugins(DeployApp)
  .dependsOn(`ocs-client`)
  .settings(
    libraryDependencies ++= Seq(
      Akka.`akka-http`,
      Akka.`akka-stream`,
      Csw.`csw-event-client`,
      Libs.`akka-http-cors`
    )
  )

lazy val `ocs-framework` = project
  .enablePlugins(JavaAppPackaging)
  .dependsOn(`sequencer-macros`, `ocs-client`)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`scala-reflect`,
      Libs.`scala-compiler`,
      Akka.`akka-stream`,
      Akka.`akka-typed-testkit` % Test,
      Ammonite.`ammonite-sshd`,
      Libs.`scala-async`.value,
      Csw.`csw-event-client`,
      Csw.`csw-time-scheduler`,
      SharedLibs.scalaTest.value % Test
    )
  )

lazy val `ocs-testkit` = project
  .dependsOn(`ocs-framework`)

lazy val `sequencer-scripts-test` = project
  .dependsOn(`ocs-framework`, `ocs-testkit`)
  .settings(
    libraryDependencies ++= Seq(
      Libs.`mockito-core`,
      SharedLibs.`scalaTest`.value % Test
    ),
    mainClass in reStart := Some("TestSequencerApp"),
    unmanagedSourceDirectories in Compile += (baseDirectory in Compile) (_ / "scripts").value,
    unmanagedSourceDirectories in Test += (baseDirectory in Test) (_ / "tests").value,
    unmanagedResourceDirectories in Compile += (baseDirectory in Compile) (_ / "configs").value,
  )

lazy val `ui-spikes` = project
  .dependsOn(`ocs-api-js`)
  .configure(baseJsSettings, bundlerSettings)
  .settings(
    npmDependencies in Compile ++= Seq(
      "svg.js" -> "2.7.1",
      "svg.pathmorphing.js" -> "0.1.3",
      "p5" -> "0.7",
      "paper" -> "v0.11.8",
      "react" -> "16.8",
      "react-dom" -> "16.8",
      "csw-aas-js" -> "0.4.11",
      "@material-ui/core" -> "3.9.3",
      "@material-ui/icons" -> "3.0.2",
    ),
    libraryDependencies ++= Seq(
      ScalablyTyped.S.svg_dot_js,
      ScalablyTyped.P.p5,
      ScalablyTyped.P.paper,
      ScalablyTyped.R.`react-facade`,
      ScalablyTyped.R.`react`,
      ScalablyTyped.R.`react-dom`,
      ScalablyTyped.C.`csw-aas-js`,
      ScalablyTyped.M.`material-ui__core`,
      ScalablyTyped.M.`material-ui__icons`,
      Enumeratum.`enumeratum`.value,
      Libs.`scala-async`.value,
      Libs.`scalajs-dom`.value,
      Libs.laminar.value,
    )
  )

lazy val baseJsSettings: Project => Project =
  _.enablePlugins(ScalaJSPlugin)
    .settings(
      scalaJSUseMainModuleInitializer := true,
      scalaJSModuleKind := ModuleKind.CommonJSModule,
      scalaJSLinkerConfig ~= { _.withESFeatures(_.withUseECMAScript2015(true)) },
        /* disabled because it somehow triggers many warnings */
      emitSourceMaps := false,
      /* in preparation for scala.js 1.0 */
      scalacOptions += "-P:scalajs:sjsDefinedByDefault",
      /* for ScalablyTyped */
      resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped"),
    )

lazy val bundlerSettings: Project => Project =
  _.enablePlugins(ScalaJSBundlerPlugin)
    .settings(
      /* Specify current versions and modes */
      startWebpackDevServer / version := "3.1.10",
      webpack / version := "4.26.1",
      Compile / fastOptJS / webpackExtraArgs += "--mode=development",
      Compile / fullOptJS / webpackExtraArgs += "--mode=production",
      Compile / fastOptJS / webpackDevServerExtraArgs += "--mode=development",
      Compile / fullOptJS / webpackDevServerExtraArgs += "--mode=production",
      useYarn := true,
      Compile / jsSourceDirectories += baseDirectory.value / "html" 
    )
