import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "WeddingApp"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "junit" % "junit-dep" % "4.11" % "test",
    "info.cukes" % "cucumber-scala" % "1.1.3" % "test",
    "info.cukes" % "cucumber-junit" % "1.1.3" % "test",
    "org.fluentlenium" % "fluentlenium-core" % "0.7.7" % "test",

    "play" %% "play-test" % "2.1.0" % "test",
    "org.specs2" %% "specs2" % "1.14" % "test"

  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
