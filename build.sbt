name := "Scalaz"

version := "0.1"

scalaVersion := "2.12.6"

//scalaVersion := "2.11.12"

val scalaTestVersion = "3.0.5"
val scalazVersion = "7.1.17"

libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",

    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalaz" %% "scalaz-effect" % scalazVersion,
    "org.scalaz" %% "scalaz-typelevel" % scalazVersion,
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test"
)