package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import org.scalamock.specs2.MockFactory
import mail.MailController
import model._
import anorm.Id
import scala.Some

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class MailNewsletterTest extends Specification with MockFactory {

  val gast1 = Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))
  val gast2 = Benutzer(Id(2L), BenutzerName("Penndorf", "Stefan"), "stefan@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."), istAdmin = true)

  "Der MailNewsletter" should {
    "allen Gästen einen Newsletter senden" in {
      val mailController = mock[MailController]
      val gästeliste = stub[Gästeliste]
      (gästeliste.vip _).when.returns(List[Benutzer](gast1, gast2))
      (mailController.sendeNewsletter _).expects(gast1)
      (mailController.sendeNewsletter _).expects(gast2)

      new MailNewsletter(gästeliste, mailController).sendNewsletter()
    }
  }
}
