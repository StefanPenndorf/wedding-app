package net.cyphoria.weddingapp.specification

import cucumber.api.scala.{DE, ScalaDsl}
import org.scalatest.matchers.ShouldMatchers._
import cucumber.api.PendingException

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class KontoRegistrierungSchritte extends ScalaDsl with DE with Browser {

  val nutzer = "Kerstin"
  val email = "kerstin@cyphoria.net"


  Angenommen("""^Kerstin hat die Einladungskarte erhalten$"""){ () =>
    // nichts zu tun hier
  }

  Angenommen("""^ruft die Registrierungsseite auf$"""){ () =>
    browser.goTo("/")
    browser.click("#registrieren")

    browser.title() should equal("Steffi und Stefan heiraten!")
    browser.$("h1").getText should equal ("Als Hochzeitsgast registrieren")
  }

  Wenn("""^sie sich mit ihren Daten für ein neues Benutzerkonto registriert$"""){ () =>

    browser.fill("#nutzername") `with` nutzer
    browser.fill("#email") `with` email
    browser.fill("#sicherheitsfrage") `with` "Albert Schweizer"

    browser.submit("#registrierung")
  }
  Dann("""^wird eine Bestätigungsseite mit einer persönlichen Begrüßung angezeigt$
       """){ () =>
  //// Express the Regexp above with the code you wish you had
    throw new PendingException()
  }
  Dann("""^erhält Sie eine E-Mail mit der Aktivierungsbestätigung$"""){ () =>
  //// Express the Regexp above with the code you wish you had
    throw new PendingException()
  }
  Dann("""^erhalten die Administratoren eine Benachrichtigung$"""){ () =>
  //// Express the Regexp above with the code you wish you had
    throw new PendingException()
  }

}
