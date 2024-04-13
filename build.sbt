ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "study-cats"
  )

libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"