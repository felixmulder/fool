lazy val fool = project.in(file(".")).
  settings(commonSettings).
  settings(compilerOptions).
  settings(replSettings).
  settings(projectStructure).
  settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    ),

    // enable improved incremental compilation algorithm
    incOptions := incOptions.value.withNameHashing(true)
  )

lazy val commonSettings = Seq(
  scalaVersion in Global := "2.12.0-RC2",
  version in Global := "0.1-SNAPSHOT",
  homepage in Global := Some(url("https://github.com/felixmulder/fool"))
)

lazy val compilerOptions = Seq(
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

lazy val replSettings = Seq(
  initialCommands in console := "import fool._"
)

lazy val projectStructure = Seq(
  scalaSource in Compile := baseDirectory.value / "src",
  javaSource in Compile := baseDirectory.value / "src",
  scalaSource in Test := baseDirectory.value / "test",
  javaSource in Test := baseDirectory.value / "test",
  resourceDirectory in Compile := baseDirectory.value / "resources"
)
