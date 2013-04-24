package net.cyphoria.weddingapp.specification.infrastructure

import play.api.test.{TestBrowser, Helpers}
import scala.Predef._
import scala.Some
import org.openqa.selenium.firefox.FirefoxDriver

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Browser extends RunningApplication {

  val webDriver: Class[FirefoxDriver] = Helpers.FIREFOX

  def browser = global[TestBrowser]("testbrowser")

  abstract override def registerGlobalHooks() {
    super.registerGlobalHooks()
    Before { _ => Unit
       registerGlobal("testbrowser", TestBrowser.of(webDriver, Some("http://localhost:" + applicationPort)))
    }

    After { _ => Unit
        browser.quit()
        unregisterGlobal("testbrowser")
    }
  }

}
