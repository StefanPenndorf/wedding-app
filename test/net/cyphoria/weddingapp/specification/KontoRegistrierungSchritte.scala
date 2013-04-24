package net.cyphoria.weddingapp.specification

import cucumber.api.scala.{DE, ScalaDsl}
import org.scalatest.matchers.ShouldMatchers._
import anorm._
import net.cyphoria.weddingapp.specification.infrastructure.{Schritte, FakeMailer, Browser}
import java.util.concurrent.TimeUnit

//import org.specs2.matcher.ShouldMatchers

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class KontoRegistrierungSchritte extends Schritte with ScalaDsl with DE with Browser with FakeMailer {

  val vorname = "Kerstin"
  val nachname = "Albert"
  val email = "kerstin@cyphoria.net"

  Angenommen("""^Kerstin hat die Einladungskarte erhalten$"""){ () =>
    // nichts zu tun hier
  }

  Angenommen("""^ruft die Registrierungsseite auf$"""){ () =>
    browser.goTo("/")
    browser.withDefaultPageWait(3, TimeUnit.SECONDS).click("#registrieren")

    browser.title() should equal("Steffi und Stefan heiraten!")
    browser.$("h1").getText should equal ("Als Hochzeitsgast registrieren")
  }

  Wenn("""^sie sich mit ihren Daten für ein neues Benutzerkonto registriert$"""){ () =>

    browser.fill("#vorname") `with` vorname
    browser.fill("#nachname") `with` nachname
    browser.fill("#email") `with` email
    browser.fill("#sicherheitsfrage") `with` "Albert Schweitzer"

    browser.submit("#registrieren")
  }

  Dann("""^wird eine Bestätigungsseite mit einer persönlichen Begrüßung angezeigt$"""){ () =>

    browser.$("h1").getText should include ("Hallo " + vorname)
    browser.$("body").getText should include ("erfolgreich registriert")
  }

  Dann("""^wird ein Konto für Sie angelegt$"""){ () =>
    val cnt = withConnection { implicit connection =>
      SQL("""select count(*) as cnt from users""").apply().head[Long]("cnt")
    }

    cnt should equal(1)
  }

  Dann("""^erhalten die Administratoren eine Benachrichtigung$"""){ () =>

    for(rcp <- Seq("stefan@cyphoria.net", "stephaniegeiler@web.de")) {
      val email = receivedEMailTo(rcp)

      email should beFrom("hochzeit@cyphoria.net")
      email should haveSubject("Kerstin hat sich registriert")
    }
  }

}
