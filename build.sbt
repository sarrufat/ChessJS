

 val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"
 val scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
 val scopt = "com.github.scopt" %% "scopt" % "3.3.0"

lazy val commonSettings = Seq(
  organization := "org.sarrufat",
  version := "0.1.0",
  scalaVersion := "2.11.6"
)


lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "ChessChallengeJS",
   libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
   libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
   libraryDependencies += "com.greencatsoft" %%% "scalajs-angular" % "0.4",
   libraryDependencies += scopt
  ). enablePlugins(ScalaJSPlugin)


//	EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

scalaJSStage in Global := FastOptStage

jsDependencies += RuntimeDOM

jsDependencies += "org.webjars" % "angularjs" % "1.3.14" / "angular-route.js" dependsOn "angular.js"
jsDependencies += "org.webjars" % "angularjs" % "1.3.14" / "angular.js"
jsDependencies += "org.webjars" % "angular-foundation" % "0.3.0" / "mm-foundation.js"
jsDependencies += "org.webjars" % "angular-foundation" % "0.3.0" / "mm-foundation-tpls.js"


persistLauncher in Compile := true

testFrameworks += new TestFramework("utest.runner.Framework")