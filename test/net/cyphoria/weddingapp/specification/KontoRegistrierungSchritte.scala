package net.cyphoria.weddingapp.specification

import cucumber.api.scala.{DE, ScalaDsl}
import org.scalatest.matchers.ShouldMatchers._
import anorm._
import net.cyphoria.weddingapp.specification.infrastructure.{Schritte, FakeMailer, Browser}
import java.util.concurrent.TimeUnit
import com.google.common.base.Predicate
import org.openqa.selenium.WebDriver
import net.cyphoria.weddingapp.specification.pages.IndexPage
import net.cyphoria.weddingapp.specification.users.User._


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class KontoRegistrierungSchritte extends Schritte with ScalaDsl with DE with Browser with FakeMailer {

  val vorname = "Kerstin"
  val nachname = "Albert"
  val emailAdresse = "kerstin@cyphoria.net"
  val passwort = "heiraten"

  var emailPasswort: Option[String] = None

  def indexPage = browser.createPage(classOf[IndexPage])


  Angenommen("""^Kerstin hat die Einladungskarte erhalten$"""){ () =>
    // nichts zu tun hier
  }

  Angenommen("""^ruft die Registrierungsseite auf$"""){ () =>
    browser.goTo("/")
    browser.withDefaultPageWait(3, TimeUnit.SECONDS).click("#registrieren")

    browser.await().atMost(3, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = browser.title() == "Steffi und Stefan heiraten!"
    })

    browser.title() should equal("Steffi und Stefan heiraten!")
    browser.$("h1").getText should equal ("Als Hochzeitsgast registrieren")
  }

  Angenommen("""^Kerstin hat sich registriert$"""){ () =>
    withConnection { implicit connection =>
      SQL("""INSERT INTO users (email, vorname, nachname) VALUES ({email},{vorname},{nachname})""")
        .on(
          "email" -> emailAdresse,
          "vorname" -> vorname,
          "nachname" -> nachname
        ).executeInsert()
    }

    assertEsWurdeEinKontoAngelegt()
  }

  Angenommen("""^Stefan hat sich angemeldet$"""){ () =>
      loadFixture("users.dbt")

      browser goTo indexPage loginAs Stefan
  }

  Wenn("""^er Kerstin freischaltet$"""){ () =>
    browser goTo "/gaesteliste"

    browser.$("h1").getText should equal ("Gästeliste")
    browser.$("""input[alt*="Kerstin freischalten"]""").click()

    browser.await().atMost(3, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        val messages: String = browser.$(".alert-message.success").getText
        messages != null && messages.contains("Kerstin wurde freigeschaltet")
      }
    })
  }


  Wenn("""^sie sich mit ihren Daten für ein neues Benutzerkonto registriert$"""){ () =>

    browser.fill("#vorname") `with` vorname
    browser.fill("#nachname") `with` nachname
    browser.fill("#email") `with` emailAdresse
    browser.fill("#sicherheitsfrage") `with` "Albert Schweitzer"

    browser.submit("#registrieren")
  }

  Wenn("""^Kerstin sich anmelden möchte$"""){ () =>
    browser.goTo("/")

    browser.fill("#loginname") `with` emailAdresse
    browser.fill("#passwort") `with` passwort
    browser.submit("#login")
  }

  Dann("""^wird eine Bestätigungsseite mit einer persönlichen Begrüßung angezeigt$"""){ () =>

    browser.$("h1").getText should include ("Hallo " + vorname)
    browser.$("body").getText should include ("erfolgreich registriert")
  }

  Dann("""^wird ein Konto für Sie angelegt$"""){ () =>
     assertEsWurdeEinKontoAngelegt()
  }

  Dann("""^erhält Kerstin eine E-Mail mit einem automatisch generierten Passwort$"""){ () =>
    val passwortPattern = "(?s).*Passwort: ([a-zA-Z0-9+~*%&$/!#;-]*).*".r
    val email = receivedEMailTo(emailAdresse)

    email should beFrom("hochzeit@cyphoria.net")
    email should haveSubject("Du wurdest als Hochzeitsgast freigeschaltet")


    val passwortPattern(emailPasswort) = email.text
    //emailPasswort = passwortPattern findFirstIn email.text
    //emailPasswort should be ('defined)
    emailPasswort should have length (12)
  }

  def assertEsWurdeEinKontoAngelegt() {
    val cnt = withConnection { implicit connection =>
      SQL("""select count(*) as cnt from users where email={email}""").on("email" -> emailAdresse).apply().head[Long]("cnt")
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

  Dann("""^erhält Kerstin eine Fehlermeldung$"""){ () =>
    browser.$(".error").getTexts.toArray.mkString("") should include ("nicht freigeschaltet")
  }

}
