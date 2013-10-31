package net.cyphoria.weddingapp.specification

import net.cyphoria.weddingapp.specification.infrastructure.{FakeMailer, Browser, Schritte}
import cucumber.api.scala.{DE, ScalaDsl}
import org.scalatest.matchers.ShouldMatchers._
import java.util.concurrent.TimeUnit
import com.google.common.base.Predicate
import org.openqa.selenium.WebDriver

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class NewsletterSchritte extends Schritte with ScalaDsl with DE with Browser with FakeMailer {

  val BETREFF = "Neues von Steffi's und Stefans Hochzeit"

  Angenommen("""^es wurden Gäste freigeschaltet$"""){ () =>
    loadFixture("gaeste-und-ein-admin.dbt")
  }

  Wenn("""^er den Newsletter verschickt$"""){ () =>
    browser goTo "/gaesteliste"

    browser.$("h1").getText should equal ("Gästeliste")
    browser.$("""input[alt*="Newsletter verschicken"]""").click()

    browser.await().atMost(3, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        val messages: String = browser.$(".alert-message.success").getText
        messages != null && messages.contains("Der Newsletter wurde gesendet.")
      }
    })
  }

  Dann("""^erhalten alle freigeschalteten Gäste den Newsletter$"""){ () =>
    for(rcp <- Seq("kerstin@cyphoria.net", "stefan@cyphoria.net")) {
      val email = receivedEMailTo(rcp)

      email should beFrom("hochzeit@cyphoria.net")
      email should haveSubject(BETREFF)
      assertNachrichtKorrekt(email.text)
    }
  }

  def assertNachrichtKorrekt(nachricht: String) {
    nachricht should include("Hallo")
    nachricht should include("Foto")
    nachricht should include("Hochzeit")
    nachricht should include("Steffi & Stefan")
  }
}
