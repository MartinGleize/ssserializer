name := "ssserializer"

version := "0.1"

scalaVersion := "2.12.10"

// https://mvnrepository.com/artifact/org.scala-lang/scala-reflect
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
// scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test