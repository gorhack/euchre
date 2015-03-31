name := "Euchre"

scalaVersion := "2.11.5"

version := "1.0"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.11+",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test")

mainClass in (Compile, run) := Some("euchre.Main")