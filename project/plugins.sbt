classpathTypes += "maven-plugin"

addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "0.6.24")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.5.0")
addSbtPlugin("com.typesafe.sbt"   % "sbt-native-packager"      % "1.3.6")
addSbtPlugin("io.spray"           % "sbt-revolver"             % "0.9.1")
addSbtPlugin("io.get-coursier"    % "sbt-coursier"             % "1.0.2")
addSbtPlugin("ch.epfl.scala"      % "sbt-scalajs-bundler"      % "0.13.1")
addSbtPlugin("ch.epfl.scala"      % "sbt-web-scalajs-bundler"  % "0.13.1")
addSbtPlugin("com.timushev.sbt"   % "sbt-updates"              % "0.3.4")
