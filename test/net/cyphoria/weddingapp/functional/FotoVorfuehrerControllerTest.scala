package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import org.scalamock.specs2.MockFactory
import controllers.WeddingAuthConfig
import play.api.test.Helpers._
import play.api.test.FakeRequest
import jp.t2v.lab.play2.auth.test.Helpers._
import net.cyphoria.weddingapp.model._


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoVorfuehrerControllerTest extends Specification with MockFactory {

  object config extends WeddingAuthConfig
  val einGast = KERSTIN

  "FotoVorfuehrer" should {

    "das Hochladen von Bildern ermoeglichen"  in laufenderAnwendungMitScenario("einemGast") {
      val result = route(FakeRequest(GET, "/fotoalben").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("Hochladen")
      contentAsString(result) must contain("""<input type="file"""")
    }

  }

}
