organization := "com.optrak"

name := "eventstore-actor-test"

version := "0.1.0-SNAPSHOT"

resolvers ++= {
  Seq(
    "sonatype.releases" at "https://oss.sonatype.org/content/repositories/releases/",
    Resolver.sonatypeRepo("snapshots"),
    "Typesafe releases Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Geotools" at "http://download.osgeo.org/webdav/geotools/"
  )
}


scalaVersion := "2.11.1"

libraryDependencies ++= {
    Seq(
      "org.slf4j" % "slf4j-api" % "1.7.7",
      "ch.qos.logback" % "logback-classic" % "1.1.2",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "joda-time" % "joda-time" % "2.4",
      "org.joda" % "joda-convert" % "1.7",
      "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.6", //2.4-SNAPSHOT is only for scala 2.10
      "com.geteventstore" %% "akka-persistence-eventstore" % "0.0.2",
      "com.geteventstore" %% "eventstore-client" % "0.5.0",
      "org.json4s" %% "json4s-native" % "3.2.10",
      "org.specs2" %% "specs2" % "2.3.12" % "test"
    )
}

parallelExecution in Test := false
