package net.cyphoria.weddingapp.templates

import play.api.test.Helpers._
import play.api.test.FakeApplication
import org.specs2.mutable.Specification
import model.{Benutzer, BenutzerName}
import play.api.mvc.Flash
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class GaestelisteTemplateTest extends Specification {

  val IGNORED = None

  val gaesteliste = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.gaesteliste(List(
      Benutzer(id=Id(1), name = BenutzerName("Teresa", "Merfert"), email = "teresa@cyphoria.net", passwort = IGNORED),
      Benutzer(id=Id(2), name = BenutzerName("Stephanie", "Geiler"), email = "steffi@cyphoria.net", passwort = IGNORED),
      Benutzer(id=Id(3), name = BenutzerName("Kerstin", "Albert"), email = "kerstin@cyphoria.net", passwort = IGNORED)
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
