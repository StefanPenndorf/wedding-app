package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model.{Gästeliste, HochzeitsplanerImpl}
import mail.MailController
import org.scalamock.specs2.MockFactory
import scala.Some


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class HochzeitsplanerImplTest extends Specification with MockFactory {

  "Der Hochzeitsplaner" should {
    "keinen Benutzer zurückgeben wenn kein Benutzer mit der ID existiert" in {
      val mailController = stub[MailController]
      val gästeliste = stub[Gästeliste]
      (gästeliste.findeGastMitId _).when(KERSTIN.id.get).returns(None)

      new HochzeitsplanerImpl(gästeliste, mailController).gastFreischalten(KERSTIN.id.get) must beNone
    }

    "einen Registranden freischalten" in DatenbankMit("einemNichtFreigeschaltetenGast") {
      val mailController = stub[MailController]
      val gästeliste = stub[Gästeliste]
      (gästeliste.findeGastMitId _).when(KERSTIN.id.get).returns(Some(KERSTIN))

      val Some(gastFreigeschaltet) = new HochzeitsplanerImpl(gästeliste, mailController).gastFreischalten(KERSTIN.id.get)

      gastFreigeschaltet.id must_==(KERSTIN.id)
    }

    "eine Passwort-Mail an den Gast verschicken wenn ein Gast freigeschaltet wird" in DatenbankMit("einemNichtFreigeschaltetenGast") {
      val gästeliste = stub[Gästeliste]
      (gästeliste.findeGastMitId _).when(*).returns(Some(KERSTIN))
      val mailController = mock[MailController]
      (mailController.sendeFreischaltungsbenachrichtigung _).expects(KERSTIN, *)

      new HochzeitsplanerImpl(gästeliste, mailController).gastFreischalten(KERSTIN.id.get) must beSome
    }


  }

}
