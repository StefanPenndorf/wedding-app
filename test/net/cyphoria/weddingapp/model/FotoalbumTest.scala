package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model.Fotoalbum

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbumTest extends Specification {

  "Fotoalbum" should {

    "kein Fotoalbum finden, wenn noch kein Foto hochgeladen wurde" in DatenbankMit("einemGast") {
        Fotoalbum.alleFotoalben() must beEmpty
    }

    "Kerstins Fotoalbum finden, wenn Sie ein Foto hochgeladen hat" in DatenbankMit("einemGastMitEinemFoto") {
      Fotoalbum.alleFotoalben().map(_.besitzer) must contain(KERSTIN)
    }

    "Kerstins Fotoalbum mit drei Fotos finden, wenn Sie drei Fotos hochgeladen hat" in DatenbankMit("einemGastMitDreiFotos") {
      Fotoalbum.alleFotoalben() must contain(Fotoalbum(KERSTIN, 3))
    }


  }

}
