val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
val actors = "com.typesafe.akka" % "akka-actor_2.10" % "2.3.14"

lazy val root = (project in file(".")).
  settings(
    name := "TP9_AKKA_ACTOR",
    version := "0.1.0",
    scalaVersion := "2.10.5",
    libraryDependencies += scalaTest,
    libraryDependencies += actors
  )
