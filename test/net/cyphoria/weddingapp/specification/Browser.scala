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

  var webDriver: Class[FirefoxDriver] = Helpers.FIREFOX
  var port: Int = Helpers.testServerPort
  var app: FakeApplication = FakeApplication()

  lazy val browser: TestBrowser = TestBrowser.of(webDriver, Some("http://localhost:" + port))
  lazy val server: TestServer = TestServer(port, app)

  Before { f: (Scenario) => Unit
    server.start()
  }

  After { f: (Scenario) => Unit
    browser.quit()
  }

  After { f: (Scenario) => Unit
    server.stop()
  }

}
