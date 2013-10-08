package net.cyphoria.weddingapp.templates

import play.api.test.Helpers._
import play.api.test.FakeApplication
import org.specs2.mutable.Specification
import play.api.mvc.Flash

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class GaestelisteTemplateTest extends Specification {

  val gaesteliste = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.gaesteliste(List(
      TERESA,
      STEPHANIE,
      KERSTIN
    ))(Flash())
  }

  "Die Gaesteliste" should {

    "alle Gaeste anzeigen" in {
      gaesteliste.body must contain("Teresa")
      gaesteliste.body must contain("Stephanie")
      gaesteliste.body must contain("Kerstin")
    }

    "die E-Mail Adressen korrekt anzeigen" in {
      gaesteliste.body must contain("<td>teresa@cyphoria.net</td>")
    }

    "einen Steuerelement zum Freischalten jedes Benutzers besitzen" in {
      gaesteliste.body must contain("""alt="Kerstin freischalten""")
    }


  }




}
