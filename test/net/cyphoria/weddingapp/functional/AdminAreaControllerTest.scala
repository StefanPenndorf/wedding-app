package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeRequest
import model._
import jp.t2v.lab.play2.auth.test.Helpers._
import controllers.{WeddingAuthConfig, AdminArea}
import org.scalamock.specs2.MockFactory
import mail.MailController
import model.BenutzerName
import anorm.Id
import model.HochzeitsGastBewerbung
import scala.Some

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class AdminAreaControllerTest extends Specification with MockFactory {

  object config extends WeddingAuthConfig

  val einGast = Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))
  val einAdmin = Benutzer(Id(1L), BenutzerName("Stefan", "Penndorf"), "stefan@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))
  val kerstin = new Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net")

  "AdminArea" should {
      "alle registrierten Gäste anzeigen" in laufenderAnwendungMitScenario("einemAdmin") {
            Bewerber bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))
            Bewerber bewirbtSichMit(HochzeitsGastBewerbung("Teresa", "Merfert", "resi@cyphoria.net"))

            val result = route(FakeRequest(GET, "/gaesteliste").withLoggedIn(config)(einAdmin.id.get)).get

            status(result) must equalTo(OK)
            contentAsString(result) must contain("Kerstin")
            contentAsString(result) must contain("Teresa")
      }

    "einen Gast freischalten" in {
      laufenderAnwendung {
        Bewerber bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))

        val result = route(FakeRequest(POST, "/gast/freischalten/1")).get

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_.contains("/gaesteliste"))
      }
    }

    "einen Registranden freischalten" in laufenderAnwendung {
        val mailController = stub[MailController]
        val gästeliste = mock[Gästeliste]
        (gästeliste.findeGastMitId _).expects(1L).returning(Some(kerstin))

        val result = new AdminArea(gästeliste, mailController).gastFreischalten(1L)(FakeRequest())

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_.contains("/gaesteliste"))
    }

    "eine Passwort-Mail an den Gast verschicken wenn ein Gast freigeschaltet wird" in laufenderAnwendung {
      val gästeliste = stub[Gästeliste]
      (gästeliste.findeGastMitId _).when(*).returns(Some(kerstin))
      val mailController = mock[MailController]
      (mailController.sendeFreischaltungsbenachrichtigung _).expects(kerstin, *)

      val result = new AdminArea(gästeliste, mailController).gastFreischalten(1L)(FakeRequest())
    }


    "den Zugriff für Gäste auf die Gästeliste verwehren" in laufenderAnwendungMitScenario("einemGast") {
      val result = route(FakeRequest(GET, "/gaesteliste").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(FORBIDDEN)
    }

    //TODO Zugriff auch noch für Freischaltung einschränken
    //TODO Funktionalität aus dem AdminArea-Controller herausrefactoren, so dass nur noch GUI-Logik bleibt
    //TODO Wer soll die Funktionalität übernehmen? Die Gästeliste?

  }

}
