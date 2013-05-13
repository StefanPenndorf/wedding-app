package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeRequest
import model._
import controllers.AdminArea
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
class AdminAreaControllerSpecification extends Specification with MockFactory {

  val kerstin = new Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net")

  "AdminArea" should {
      "alle registrierten Gäste anzeigen"  in {
        laufenderAnwendung {
            Bewerber bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))
            Bewerber bewirbtSichMit(HochzeitsGastBewerbung("Teresa", "Merfert", "resi@cyphoria.net"))

            val result = route(FakeRequest(GET, "/gaesteliste")).get

            status(result) must equalTo(OK)
            contentAsString(result) must contain("Kerstin")
            contentAsString(result) must contain("Teresa")
        }
      }

      "alle registrierten Gäste anzeigen"  in {
          val benutzerliste = List(
            new Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net"),
            new Benutzer(Id(2L), BenutzerName("Teresa", "Merfert"), "resi@cyphoria.net")
          )

          val mailController = stub[MailController]
          val gästeliste = mock[Gästeliste]
          (gästeliste.gäste _).expects.returning(benutzerliste)

          val result = new AdminArea(gästeliste, mailController).gaesteliste()(FakeRequest())

          status(result) must equalTo(OK)
          contentAsString(result) must contain("Kerstin")
          contentAsString(result) must contain("Teresa")
      }

    "einen Gast freischalten" in {
      laufenderAnwendung {
        Bewerber bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))

        val result = route(FakeRequest(POST, "/gast/freischalten/1")).get

        status(result) must equalTo(SEE_OTHER)
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


  }

}
