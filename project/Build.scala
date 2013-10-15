import com.typesafe.config.ConfigFactory
import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val conf = ConfigFactory.parseFile(new File("conf/version.conf")).resolve()

  val appName         = "WeddingApp"
  val appVersion      = conf.getString("app.version")

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.24",

    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",

    "org.slf4j" % "jul-to-slf4j" % "1.7.2",
    "ch.qos.logback" % "logback-classic" % "1.0.7",

    "jp.t2v" %% "play2.auth"      % "0.9",
    "org.mindrot" % "jbcrypt"  % "0.3m",

    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1",

    "jmimemagic" % "jmimemagic" % "0.1.2",
    "xml-apis" % "xml-apis" % "1.4.01" force(),


    // TEST DEPENDENCIES
    "play" %% "play-test" % "2.1.2" % "test",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
    "org.scalamock" %% "scalamock-specs2-support" % "3.0.1" % "test",
    "jp.t2v" %% "play2.auth.test" % "0.9" % "test",

    "junit" % "junit-dep" % "4.11" % "test",
    "info.cukes" % "cucumber-scala" % "1.1.2" % "test",
    "info.cukes" % "cucumber-junit" % "1.1.2" % "test",
    "org.fluentlenium" % "fluentlenium-core" % "0.8.0" % "test",
    "org.seleniumhq.selenium" % "selenium-java" % "2.32.0" % "test",

    "com.icegreen" % "greenmail" % "1.3.1b" % "test",

    // Dependencies for scaladbtest
    "org.scalaj" % "scalaj-collection_2.9.1" % "1.2" % "test",
    "org.springframework" % "spring-jdbc" % "3.0.7.RELEASE" % "test"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    routesImport += "binders._"
  )

}
