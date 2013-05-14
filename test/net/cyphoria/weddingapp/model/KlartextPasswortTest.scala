package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model.KlartextPasswort

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class KlartextPasswortTest  extends Specification {

  "Ein Klartextpasswort" should {
    "12 Zeichen lang sein" in {
      KlartextPasswort.generate.passwort must have length(12)
    }

    "sollte nur aus Buchstaben, Zahlen, Bindestrich und Ausrufezeichen bestehen" in {
      KlartextPasswort.generate.passwort must beMatching("""^[A-Za-z0-9-!]+$""")
    }

    "sollte jedes mal anders sein" in {
      val pw1 = KlartextPasswort.generate
      val pw2 = KlartextPasswort.generate

      pw1 must not(be_==(pw2))
    }

    "sollte das Passwort als String-Representation haben" in  {
      val pw = KlartextPasswort.generate
      pw.toString must be_==(pw.passwort)
    }

  }
}
