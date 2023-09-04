val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

lazy val root = (project in file(".")).
  settings(
    name := "TP3_Structure",
    version := "0.1.0",
    scalaVersion := "2.10.5",
    libraryDependencies += scalaTest
  )
