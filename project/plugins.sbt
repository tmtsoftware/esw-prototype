classpathTypes += "maven-plugin"

addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "0.6.28")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.0")
addSbtPlugin("com.typesafe.sbt"   % "sbt-native-packager"      % "1.3.21")
addSbtPlugin("io.spray"           % "sbt-revolver"             % "0.9.1")
addSbtPlugin("ch.epfl.scala"      % "sbt-scalajs-bundler"      % "0.15.0-0.6")
addSbtPlugin("com.timushev.sbt"   % "sbt-updates"              % "0.4.0")

addSbtCoursier
resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped")
addSbtPlugin("org.scalablytyped" % "sbt-scalablytyped" % "202001240947")
