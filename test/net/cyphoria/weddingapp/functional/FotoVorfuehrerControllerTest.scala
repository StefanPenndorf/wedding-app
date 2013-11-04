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

    "ein Foto mit korrektem Mime-Type für PNG zurück geben" in laufenderAnwendungMitScenario("einemGastMitEinemFoto") {
      val result = route(FakeRequest(GET, "/foto/1").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentType(result) must beSome("image/png")
    }

    "eine Fehlermeldung anzeigen, wenn das angeforderte Foto nicht existiert" in laufenderAnwendungMitScenario("einemGast") {
      val result = route(FakeRequest(GET, "/foto/999").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(NOT_FOUND)
      contentAsString(result) must contain("Foto nicht gefunden")
    }

    "ein Foto mit korrektem Mime-Type für JPEG zurück geben" in laufenderAnwendungMitScenario("einemGastMitDreiFotos") {
      val result = route(FakeRequest(GET, "/foto/2").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentType(result) must beSome("image/jpeg")
    }


    "ein Foto zurück geben" in laufenderAnwendungMitScenario("einemGastMitEinemFoto") {
      val result = route(FakeRequest(GET, "/foto/1").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentAsBytes(result) must beEqualTo(PNG_IMAGE_CONTENT)
    }

    "die erste Seite vom Fotoalbum anzeigen" in laufenderAnwendungMitScenario("einemGastMitDreiFotos") {
      val result = route(FakeRequest(GET, "/fotoalbum/Kerstin.Albert").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("Fotoalbum von Kerstin")
      contentAsString(result) must contain("src=\"/foto/1\"")
      contentAsString(result) must contain("src=\"/foto/2\"")
      contentAsString(result) must contain("src=\"/foto/3\"")
    }


    "die Fotoseite mit dem ersten Foto im Album anzeigen" in laufenderAnwendungMitScenario("einemGastMitEinemFoto") {
      val result = route(FakeRequest(GET, "/fotoalbum/Kerstin.Albert/foto").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("Fotoalbum von Kerstin")
      contentAsString(result) must contain("src=\"/foto/1\"")

    }


  }

}
