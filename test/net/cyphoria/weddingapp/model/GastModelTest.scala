package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model._
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class GastModelTest extends Specification {

  val einGast = Benutzer(Id(1L), BenutzerName("Stefan", "Penndorf"), "stefan@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))
  val gästeliste = new PersistenteGästeliste

  "Ein Gast" should {

    "auf der Gästeliste stehen" in DatenbankMit("einemGast") {
      gästeliste.gäste must contain(einGast)
    }

    "auf der Gästeliste unter der angegebenen E-Mail stehen" in DatenbankMit("einemGast") {
         val derGast = gästeliste.findeGastMitEMail(einGast.email).get

         derGast must_== einGast
    }

    "auf der Gästeliste unter dem angegebenen Namen stehen" in DatenbankMit("einemGast") {
      val derGast = gästeliste.findeGastMitName(einGast.name).get

      derGast must_== einGast
    }


    "auf der Gästeliste unter der angegebenen Id stehen" in DatenbankMit("einemGast") {
      val derGast = gästeliste.findeGastMitId(1L).get

      derGast must_== einGast
    }


  }

}
