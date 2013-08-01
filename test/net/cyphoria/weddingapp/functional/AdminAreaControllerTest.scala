package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeRequest
import model._
import jp.t2v.lab.play2.auth.test.Helpers._
import controllers.WeddingAuthConfig
import org.scalamock.specs2.MockFactory
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
            Bewerber bewirbtSichMit HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net")
            Bewerber bewirbtSichMit HochzeitsGastBewerbung("Teresa", "Merfert", "resi@cyphoria.net")

            val result = route(FakeRequest(GET, "/gaesteliste").withLoggedIn(config)(einAdmin.id.get)).get

            status(result) must equalTo(OK)
            contentAsString(result) must contain("Kerstin")
            contentAsString(result) must contain("Teresa")
      }

      "einen Gast freischalten" in laufenderAnwendungMitScenario("einemAdmin") {
          Bewerber bewirbtSichMit HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net")

          val result = route(FakeRequest(POST, "/gast/freischalten/1").withLoggedIn(config)(einAdmin.id.get)).get

          status(result) must equalTo(SEE_OTHER)
          redirectLocation(result) must beSome.which(_.contains("/gaesteliste"))
      }

      "eine Fehlermeldung anzeigen wenn der Gast der freigeschaltet werden soll nicht existiert" in laufenderAnwendungMitScenario("einemAdmin") {
        Bewerber bewirbtSichMit HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net")

        val result = route(FakeRequest(POST, "/gast/freischalten/42").withLoggedIn(config)(einAdmin.id.get)).get

        status(result) must equalTo(NOT_FOUND)
        contentAsString(result) must contain("Kerstin")
      }

      "den Zugriff für Gäste auf die Gästeliste verwehren" in laufenderAnwendungMitScenario("einemGast") {
        val result = route(FakeRequest(GET, "/gaesteliste").withLoggedIn(config)(einGast.id.get)).get

        status(result) must equalTo(FORBIDDEN)
      }

      "den Zugriff für Gäste auf die Freischaltung verwehren" in laufenderAnwendungMitScenario("einemGast") {
        val result = route(FakeRequest(POST, "/gast/freischalten/1").withLoggedIn(config)(einGast.id.get)).get

        status(result) must equalTo(FORBIDDEN)
      }

      "einen Newletter verschicken" in laufenderAnwendungMitScenario("einemAdmin") {
        val result = route(FakeRequest(POST, "/newsletter").withLoggedIn(config)(einAdmin.id.get)).get

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_.contains("/gaesteliste"))
        flash(result).get("erfolgsMeldung").get must contain("Newsletter wurde gesendet")
      }



  }

}
