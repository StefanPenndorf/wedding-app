package net.cyphoria.weddingapp.specification.infrastructure

import play.api.test.TestBrowser
import scala.Predef._
import scala.Some
import net.cyphoria.weddingapp.specification.seiten.StartSeite
import com.saucelabs.selenium.client.factory.SeleniumFactory
import org.openqa.selenium.remote.DesiredCapabilities

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Browser extends RunningApplication {

  def browser = global[TestBrowser]("testbrowser")
  def startSeite = browser.createPage(classOf[StartSeite])

  abstract override def registerGlobalHooks() {
    super.registerGlobalHooks()
    Before { _ => Unit
       registerGlobal("testbrowser",
         new TestBrowser(
            SeleniumFactory.createWebDriver(DesiredCapabilities.firefox()),
            Some("http://localhost:" + applicationPort)))
    }

    After { _ => Unit
        browser.quit()
        unregisterGlobal("testbrowser")
    }
  }

}
