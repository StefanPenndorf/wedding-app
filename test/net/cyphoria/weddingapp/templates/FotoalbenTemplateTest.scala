package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeApplication
import model.Fotoalbum
import play.api.mvc.Flash

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbenTemplateTest extends Specification {

  val fotoalben = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.fotoalben(List(
      Fotoalbum(TERESA, 5),
      Fotoalbum(STEPHANIE, 3),
      Fotoalbum(KERSTIN, 0)
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
