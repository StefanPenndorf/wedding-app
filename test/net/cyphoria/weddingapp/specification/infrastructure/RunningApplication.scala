package net.cyphoria.weddingapp.specification.infrastructure

import cucumber.api.scala.ScalaDsl
import play.api.test.{TestServer, FakeApplication, Helpers}
import play.api.test.Helpers._
import play.api.db.DB

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait RunningApplication extends HookRegistering with ScalaDsl {

  val applicationPort: Int = Helpers.testServerPort

  def server = global[TestServer]("server")
  def application = global[FakeApplication]("application")

  def withConnection[A](block : Function1[java.sql.Connection, A]) = {
    DB.withConnection(block)(application)
  }

  abstract override def registerGlobalHooks() {
    super.registerGlobalHooks()
    Before { _ => Unit
      val application = FakeApplication(additionalConfiguration = inMemoryDatabase())
      val server = TestServer(applicationPort, application)
      server.start()

      registerGlobal("application", application)
      registerGlobal("server", server)
    }

    After { _ => Unit
      server.stop()
      unregisterGlobal("server")
      unregisterGlobal("application")
    }
  }


}
