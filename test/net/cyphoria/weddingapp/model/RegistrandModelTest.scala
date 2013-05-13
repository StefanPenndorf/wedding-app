package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class RegistrandModelTest extends Specification {

  val stefansEMail = "stefan@cyphoria.net"
  val gästeliste = new PersistenteGästeliste

  "Ein registrierter Registrand" should {
    "auf der Gästeliste stehen" in mitDatenbank {
      val stefan = Registrand("stefan", "penndorf", stefansEMail)
      stefan registrieren()

      val gast = gästeliste.findeGastMitEMail(stefansEMail)
      gast must beSome.which(_.name == stefan.benutzerName)
    }
  }


}
