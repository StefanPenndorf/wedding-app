package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeRequest
import model.{HochzeitsGastBewerbung, Benutzer}
import controllers.AdminArea

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class AdminAreaControllerSpecification extends Specification {

  "AdminArea" should {
      "alle registrierten Gäste anzeigen"  in {
        laufenderAnwendung {
            Benutzer bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))
            Benutzer bewirbtSichMit(HochzeitsGastBewerbung("Teresa", "Merfert", "resi@cyphoria.net"))

            val request = FakeRequest()
            val result = AdminArea.gaesteliste()(request)

            status(result) must equalTo(OK)
            contentAsString(result) must contain("Kerstin")
            contentAsString(result) must contain("Teresa")
        }
      }

    "einen Gast freischalten"  in {
      laufenderAnwendung {
        Benutzer bewirbtSichMit(HochzeitsGastBewerbung("Kerstin", "Albert", "kerstin@cyphoria.net"))

        val request = FakeRequest()
        val result = AdminArea.gastFreischalten(1)(request)

        status(result) must equalTo(SEE_OTHER)
      }
    }


  }

}
