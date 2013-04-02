import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "WeddingApp"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,

    "org.slf4j" % "jul-to-slf4j" % "1.6.6",
    "ch.qos.logback" % "logback-classic" % "0.9.11",

    "junit" % "junit-dep" % "4.11" % "test",
    "info.cukes" % "cucumber-scala" % "1.1.2" % "test",
    "info.cukes" % "cucumber-junit" % "1.1.2" % "test",
    "org.fluentlenium" % "fluentlenium-core" % "0.7.7" % "test",

    "org.seleniumhq.selenium" % "selenium-java" % "2.31.0" % "test",


    "play" %% "play-test" % "2.1.0" % "test",
    "org.specs2" %% "specs2" % "1.14" % "test"

  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
