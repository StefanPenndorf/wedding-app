package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeApplication
import model.{BenutzerName, Benutzer}
import anorm.Id
import play.api.mvc.Flash

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbenTemplateTest extends Specification {

  val IGNORED = None

  val fotoalben = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.fotoalben(List(
      (Benutzer(id=Id(1), name = BenutzerName("Teresa", "Merfert"), email = "teresa@cyphoria.net", passwort = IGNORED), 5),
      (Benutzer(id=Id(2), name = BenutzerName("Stephanie", "Geiler"), email = "steffi@cyphoria.net", passwort = IGNORED), 3),
      (Benutzer(id=Id(3), name = BenutzerName("Kerstin", "Albert"), email = "kerstin@cyphoria.net", passwort = IGNORED), 0)
    ))(Flash())
  }

  "Die Fotoalbenübersicht" should {
      "alle Gäste mit Fotoalbum anzeigen" in {
        fotoalben.body must contain("Teresa")
        fotoalben.body must contain("Stephanie")
        fotoalben.body must contain("Kerstin")
      }

      "anzeigen wie viele Fotos ein Gast in sein Album hochgeladen hat" in {
        fotoalben.body must contain("Teresa (5 Bilder)")
      }


  }

}
