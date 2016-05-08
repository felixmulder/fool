import sbt._
import Keys._
import scoverage.ScoverageKeys._

object FoolBuild extends Build {
  override def settings: Seq[Setting[_]] = {
    super.settings ++ Seq(
      // Exclude "predef" from coverage results
      coverageExcludedFiles := "src/fool/package.scala;",
      scalaVersion in Global := "2.11.8",
      version in Global := "0.1-SNAPSHOT",
      homepage in Global := Some(url("https://github.com/felixmulder/fool")),

      scalacOptions in Global ++= Seq(
        "-feature",
        "-deprecation",
        "-encoding",
        "utf8",
        "-language:_",
        "-Yno-predef",
        "-Yno-imports"
      )
    )
  }

  lazy val fool = project.in(file(".")).
    settings(
      /** Project structure */
      scalaSource in Compile := baseDirectory.value / "src",
      javaSource in Compile := baseDirectory.value / "src",
      scalaSource in Test := baseDirectory.value / "test",
      javaSource in Test := baseDirectory.value / "test",
      resourceDirectory in Compile := baseDirectory.value / "resources",

      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.2.6" % "test"
      ),

      // enable improved incremental compilation algorithm
      incOptions := incOptions.value.withNameHashing(true)
    )
}
