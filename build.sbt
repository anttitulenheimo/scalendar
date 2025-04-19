ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.3"

lazy val root = (project in file("."))
  .settings(name := "Scalender")

libraryDependencies += "org.scalafx" % "scalafx_3" % "22.0.0-R33"

libraryDependencies += "org.mnode.ical4j" % "ical4j" % "4.1.0"

libraryDependencies += "org.controlsfx" % "controlsfx" % "11.1.2"
