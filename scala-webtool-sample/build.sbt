name := "scala-webtool-sample"
//version := "0.1"
scalaVersion := "2.11.7"
scalacOptions += "-feature"
//scalacOptions += "-unchecked"
scalacOptions += "-deprecation"

lazy val root = (project in file("."))
	.enablePlugins(PlayScala)

val akkaVersion = "2.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-distributed-data-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  //"ch.qos.logback" % "logback-classic" % "1.1.5",
  //"org.slf4j" % "slf4j-log4j12" % "1.7.16",
  //"org.slf4j" % "slf4j-simple" % "1.7.16",
  //"javax.servlet" % "javax.servlet-api" % "4.0.0-b01",
  jdbc,
  cache,
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := true
