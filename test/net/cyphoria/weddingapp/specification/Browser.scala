package net.cyphoria.weddingapp.specification

import play.api.test.{FakeApplication, TestServer, TestBrowser, Helpers}
import play.api.test.Helpers._
import scala.Predef._
import scala.Some
import cucumber.api.scala.ScalaDsl
import cucumber.api.Scenario
import org.openqa.selenium.firefox.FirefoxDriver
import play.api.db.DB

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Browser extends ScalaDsl {

  def browser = Browser.browser.get

  Browser.register(this)

  def registerHooks() {
    Before { f: (Scenario) => Unit
      Browser.startServer()
    }

    Before { f: (Scenario) => Unit
      Browser.startBrowser()
    }

    After { f: (Scenario) => Unit
       Browser.stopBrowser()
    }

    After { f: (Scenario) => Unit
       Browser.stopServer()
    }
  }

  def withConnection[A](block : Function1[java.sql.Connection, A]) = {
    DB.withConnection(block)(Browser.application.get)
  }

}

object Browser {

  val webDriver: Class[FirefoxDriver] = Helpers.FIREFOX
  val port: Int = Helpers.testServerPort

  var browser: Option[TestBrowser] = Option.empty
  var server: Option[TestServer] = Option.empty
  var application: Option[FakeApplication] = Option.empty

  var hooksRegistered = false

  def register(that: Browser) {
      if (!hooksRegistered) {
        that.registerHooks()
        hooksRegistered = true
      }
  }

  def startBrowser() {
    browser = Option(TestBrowser.of(webDriver, Some("http://localhost:" + port)))
  }

  def stopBrowser() {
    browser.get.quit()
    browser = Option.empty
  }

  def startServer() {
    application = Option(FakeApplication(additionalConfiguration = inMemoryDatabase()))
    server = Option(TestServer(port, application.get))
    server.get.start()
  }

  def stopServer() {
    server.get.stop()
    server = Option.empty
    application = Option.empty
  }

}