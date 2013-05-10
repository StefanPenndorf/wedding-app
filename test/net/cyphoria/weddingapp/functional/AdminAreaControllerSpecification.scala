package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeRequest
import model.{BenutzerName, BenutzerRepository, HochzeitsGastBewerbung, Benutzer}
import controllers.AdminArea
import org.scalamock.specs2.MockFactory
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class AdminAreaControllerSpecification extends Specification with MockFactory {

  "AdminArea" should {
      "alle registrierten Gäste anzeigen"  in {
        laufenderAnwendung {
            Benutzer bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))
            Benutzer bewirbtSichMit(HochzeitsGastBewerbung("Teresa", "Merfert", "resi@cyphoria.net"))

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

          val benutzerRep = mock[BenutzerRepository]
          (benutzerRep.alleBenutzer _).expects().returning(benutzerliste)

          val result = new AdminArea(benutzerRep).gaesteliste()(FakeRequest())

          status(result) must equalTo(OK)
          contentAsString(result) must contain("Kerstin")
          contentAsString(result) must contain("Teresa")
      }



    "einen Gast freischalten"  in {
      laufenderAnwendung {
        Benutzer bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))

        val result = route(FakeRequest(POST, "/gast/freischalten/1")).get

        status(result) must equalTo(SEE_OTHER)
      }
    }


  }

}
