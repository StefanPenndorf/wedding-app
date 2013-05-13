package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeRequest

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class RegistrierungControllerSpecification extends Specification {

  "Registrierung" should {
    "eine Fehlermeldung anzeigen, wenn bereits ein Benutzer mit der gewünschten E-Mail-Adresse registriert ist" in {
      laufenderAnwendung {
          val request = FakeRequest().withFormUrlEncodedBody(
            "vorname" -> "Carlos",
            "nachname" -> "Santana",
            "email" -> "carlos@santana.com",
            "sicherheitsfrage" -> "Albert Schweitzer"
          )

          val firstResult = controllers.Registrierung.registrieren()(request)
          status(firstResult) must equalTo(SEE_OTHER)

          val result = controllers.Registrierung.registrieren()(request)

          status(result) must equalTo(BAD_REQUEST)
          contentAsString(result) must contain("E-Mail-Adresse wird bereits verwendet")
      }
    }


    "eine Fehlermeldung anzeigen, wenn bereits ein Benutzer mit diesem Namen registriert ist" in {
      laufenderAnwendung {
        val request = FakeRequest().withFormUrlEncodedBody(
          "vorname" -> "Carlos",
          "nachname" -> "Santana",
          "email" -> "carlos@santana.com",
          "sicherheitsfrage" -> "Albert Schweitzer"
        )

        val firstResult = controllers.Registrierung.registrieren()(request)
        status(firstResult) must equalTo(SEE_OTHER)

        val result = controllers.Registrierung.registrieren()(request)

        status(result) must equalTo(BAD_REQUEST)
        contentAsString(result) must contain("Ein Gast mit diesem Namen ist bereits registriert")
      }
    }
  }

}
