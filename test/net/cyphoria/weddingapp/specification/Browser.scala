package net.cyphoria.weddingapp.specification

import play.api.test.{FakeApplication, TestServer, TestBrowser, Helpers}
import scala.Predef._
import scala.Some
import cucumber.api.scala.ScalaDsl
import cucumber.api.Scenario
import org.openqa.selenium.firefox.FirefoxDriver

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Browser extends ScalaDsl {

  lazy val browser = Browser.browser.get
  lazy val server = Browser.server

  Browser.register(this)

  def registerHooks() {
    Before { f: (Scenario) => Unit
      Browser.server.start()
    }

    Before { f: (Scenario) => Unit
      Browser.startBrowser()
    }

    After { f: (Scenario) => Unit
       Browser.stopBrowser()
    }

    After { f: (Scenario) => Unit
       Browser.server.stop()
    }
  }

}

object Browser {

  var webDriver: Class[FirefoxDriver] = Helpers.FIREFOX
  var port: Int = Helpers.testServerPort
  var app: FakeApplication = FakeApplication()

  var browser: Option[TestBrowser] = Option.empty
  lazy val server: TestServer = TestServer(port, app)

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
  }

}