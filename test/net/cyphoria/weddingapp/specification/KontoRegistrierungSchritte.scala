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
  val email = "kerstin@cyphoria.net"
  val passwort = "heiraten"

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
          "email" -> email,
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


  Wenn("""^sie sich mit ihren Daten für ein neues Benutzerkonto registriert$"""){ () =>

    browser.fill("#vorname") `with` vorname
    browser.fill("#nachname") `with` nachname
    browser.fill("#email") `with` email
    browser.fill("#sicherheitsfrage") `with` "Albert Schweitzer"

    browser.submit("#registrieren")
  }

  Wenn("""^Kerstin sich anmelden möchte$"""){ () =>
    browser.goTo("/")

    browser.fill("#loginname") `with` email
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

  def assertEsWurdeEinKontoAngelegt() {
    val cnt = withConnection { implicit connection =>
      SQL("""select count(*) as cnt from users where email={email}""").on("email" -> email).apply().head[Long]("cnt")
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
