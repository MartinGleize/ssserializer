name := "ssserializer"

version := "0.1"

scalaVersion := "2.13.0"

// https://mvnrepository.com/artifact/org.scala-lang/scala-reflect
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
// scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
// Apache Commons Text
// https://mvnrepository.com/artifact/org.apache.commons/commons-text
libraryDependencies += "org.apache.commons" % "commons-text" % "1.7"