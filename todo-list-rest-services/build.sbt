name := """todo-list-rest-services"""
organization := "com.yuanzhengli"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "com.h2database" % "h2" % "1.4.200",
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
)
libraryDependencies += guice
