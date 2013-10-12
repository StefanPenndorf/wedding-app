package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeApplication
import model.{Foto, Fotoalbum}
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoTemplateTest extends Specification {

  val foto = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.foto(Fotoalbum(TERESA, 1), Foto(Id(1)))
  }

  "Die Fotoansicht" should {

    "den Namen des Albumbesitzers ausweisen" in  {
      foto.body must contain("Fotoalbum von Teresa")
    }



  }

}
