name := "akka-101"

version := "1.0"

scalaVersion in ThisBuild := "2.11.7"

resolvers in ThisBuild ++= Seq(
  DefaultMavenRepository,
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases"),
  Resolver.typesafeRepo("releases"))

libraryDependencies ++= {
  val akkaVersion = "2.4.14"
  Seq(
    "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"   % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster"   % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion  % "test",
    "org.scalatest"     %% "scalatest"    % "3.0.0"      % "test",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M3"
  )
}
