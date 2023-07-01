import Dependencies._

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.ibtisam"
ThisBuild / organizationName := "ibtisam"

lazy val root = (project in file("."))
  .settings(
    name := "shopping_basket2",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test

  )

