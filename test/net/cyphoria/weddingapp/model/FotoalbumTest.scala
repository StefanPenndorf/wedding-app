package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model.{Foto, Fotoalbum, PersistenterFotoalbenVerwalter}
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbumTest extends Specification {

  val einGast = KERSTIN

  "Fotoalbum eines Gastes" should {
    "das erste Foto zurück geben" in DatenbankMit("einemGastMitEinemFoto") {
      einGast.fotoalbum.get.erstesFoto must beEqualTo(Foto(Id(1), PNG_IMAGE_CONTENT))
    }

    "das Foto mit der kleinsten ID als erstes Foto zurück geben, selbst wenn drei Fotos hochgeladen wurden" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.erstesFoto must beEqualTo(Foto(Id(1), PNG_IMAGE_CONTENT))
    }

  }

  val verwalter = new PersistenterFotoalbenVerwalter()

  "PersistenterFotoalbenVerwalter" should {

    "kein Fotoalbum finden, wenn noch kein Foto hochgeladen wurde" in DatenbankMit("einemGast") {
      verwalter.alleFotoalben() must beEmpty
    }

    "findet alle Fotoalben von Benutzern, die ein Foto hochgeladen haben" in DatenbankMit("einemGastMitEinemFoto") {
      verwalter.alleFotoalben().map(_.besitzer) must contain(KERSTIN)
    }

    "Kerstins Fotoalbum mit drei Fotos finden, wenn Sie drei Fotos hochgeladen hat" in DatenbankMit("einemGastMitDreiFotos") {
      verwalter.alleFotoalben() must contain(Fotoalbum(KERSTIN, 3))
    }

  }

}
