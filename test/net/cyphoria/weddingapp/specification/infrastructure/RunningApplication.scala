package net.cyphoria.weddingapp.specification.infrastructure

import cucumber.api.scala.ScalaDsl
import play.api.test.{TestServer, FakeApplication, Helpers}
import play.api.test.Helpers._
import play.api.db.DB
import cucumber.api.Scenario
import scaladbtest.test.ScalaDbTester
import javax.sql.DataSource

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait RunningApplication extends HookRegistering with ScalaDsl {

  val applicationPort: Int = Helpers.testServerPort

  def server = global[TestServer]("server")
  def application = global[FakeApplication]("application")
  def scaladbtester = global[ScalaDbTester]("scaladbtester")

  def loadFixture(path: String) {
     scaladbtester.onBefore(path)
  }

  def withConnection[A](block : Function1[java.sql.Connection, A]) = {
    DB.withConnection(block)(application)
  }

  abstract override def registerGlobalHooks() {
    super.registerGlobalHooks()
    Before { _ => Unit
      val application = FakeApplication(additionalConfiguration = applicationConfiguration())
      val server = TestServer(applicationPort, application)
      server.start()

      registerGlobal("application", application)
      registerGlobal("server", server)

      val source: DataSource = DB.getDataSource()(application)
      val scaladbtester = new ScalaDbTester(source, "test/resources/")
      registerGlobal("scaladbtester", scaladbtester)
    }

    After { f: Scenario => Unit
      scaladbtester.onAfter()
      unregisterGlobal("scaladbtester")

      server.stop()
      unregisterGlobal("server")
      unregisterGlobal("application")
    }
  }

  private def applicationConfiguration() = {
    inMemoryDatabase() ++ Map(
      "smtp.host" -> "localhost",
      "smtp.port" -> "3025",
      "smtp.mock" -> "false"
    )
  }


}
