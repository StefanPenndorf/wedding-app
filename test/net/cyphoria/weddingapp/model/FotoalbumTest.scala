package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model.{Fotoalbum, PersistenterFotoalbenVerwalter}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbumTest extends Specification {

  val verwalter = new PersistenterFotoalbenVerwalter()

  "PersistenterFotoalbenVerwalter" should {

    "kein Fotoalbum finden, wenn noch kein Foto hochgeladen wurde" in DatenbankMit("einemGast") {
      verwalter.alleFotoalben() must beEmpty
    }

    "Kerstins Fotoalbum nicht finden, wenn noch kein Foto hochgeladen wurde" in DatenbankMit("einemGast") {
      verwalter.findeFotoalbumVon(KERSTIN) must beNone
    }


    "findet alle Fotoalben von Benutzern, die ein Foto hochgeladen haben" in DatenbankMit("einemGastMitEinemFoto") {
      verwalter.alleFotoalben().map(_.besitzer) must contain(KERSTIN)
    }

    "Kerstins Fotoalbum finden, wenn Sie ein Foto hochgeladen hat" in DatenbankMit("einemGast") {
      verwalter.speichereFoto(new Array[Byte](3), Fotoalbum(KERSTIN, 0))
      verwalter.findeFotoalbumVon(KERSTIN) must beSome(Fotoalbum(KERSTIN, 1))
    }


    "Kerstins Fotoalbum mit drei Fotos finden, wenn Sie drei Fotos hochgeladen hat" in DatenbankMit("einemGastMitDreiFotos") {
      verwalter.findeFotoalbumVon(KERSTIN) must beSome(Fotoalbum(KERSTIN, 3))
      verwalter.alleFotoalben() must contain(Fotoalbum(KERSTIN, 3))
    }


  }

}
