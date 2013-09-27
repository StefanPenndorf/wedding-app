package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import org.scalamock.specs2.MockFactory
import controllers.WeddingAuthConfig
import play.api.test.Helpers._
import play.api.test.FakeRequest
import model.{BenutzerName, Benutzer}
import anorm.Id
import jp.t2v.lab.play2.auth.test.Helpers._
import org.apache.http.entity.mime.MultipartEntity
import java.io.File
import org.apache.http.entity.mime.content.{FileBody, StringBody}
import java.nio.charset.Charset


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoVorfuehrerControllerTest extends Specification with MockFactory {

  object config extends WeddingAuthConfig

  val einGast = Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))


  "FotoVorfuehrer" should {

    "das Hochladen von Bildern ermoeglichen"  in laufenderAnwendungMitScenario("einemGast") {
      val result = route(FakeRequest(GET, "/fotoalben").withLoggedIn(config)(einGast.id.get)).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("Hochladen")
      contentAsString(result) must contain("""<input type="file"""")
    }

    def makeMultiPart(fileName: String, contentType: String) = {
      val entity = new MultipartEntity()
      val fileSaved = new File(fileName)
      entity.addPart("message", new StringBody("spreadsheet", Charset.forName("UTF-8")))
      entity.addPart("upload", new FileBody(fileSaved, contentType))
      entity
    }

  }

}
