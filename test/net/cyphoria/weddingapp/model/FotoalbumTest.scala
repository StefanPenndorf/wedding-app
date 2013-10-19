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

  val erstesFoto: Foto = Foto(Id(1), PNG_IMAGE_CONTENT)
  val zweitesFoto: Foto = Foto(Id(2), JPEG_IMAGE_CONTENT)
  val drittesFoto: Foto = Foto(Id(3), PNG_IMAGE_CONTENT)

  "Fotoalbum eines Gastes" should {

    "das erste Foto zurück geben" in DatenbankMit("einemGastMitEinemFoto") {
      einGast.fotoalbum.get.erstesFoto must beEqualTo(erstesFoto)
      einGast.fotoalbum.get.fotoMitPosition(1) must beSome(erstesFoto)
    }

    "das Foto mit der kleinsten ID als Foto mit Nummer 1 zurück geben, selbst wenn drei Fotos hochgeladen wurden" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.erstesFoto must beEqualTo(erstesFoto)
      einGast.fotoalbum.get.fotoMitPosition(1) must beSome(erstesFoto)
    }

    "das zweite Foto als Foto mit Fotonummer 2 zurück geben" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.fotoMitPosition(2) must beSome(zweitesFoto)
    }

    "kein Foto mit negativer Fotonummer finden" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.fotoMitPosition(-1) must beNone
    }

    "kein nächstes Foto für ein einziges Foto haben" in DatenbankMit("einemGastMitEinemFoto") {
      einGast.fotoalbum.get.naechstePosition(erstesFoto) must beNone
    }

    "kein nächstes Foto für das letzte Foto haben" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.naechstePosition(drittesFoto) must beNone
    }

    "das zweite Foto als nächstes Foto für das erste Foto haben" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.naechstePosition(erstesFoto) must beSome(2)
    }

    "kein vorhergehendes Foto für das erste Foto haben" in DatenbankMit("einemGastMitEinemFoto") {
      einGast.fotoalbum.get.vorhergehendePosition(erstesFoto) must beNone
    }

    "das erste Foto als vorhergehendes Foto für das zweite Foto haben" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum.get.vorhergehendePosition(zweitesFoto) must beSome(1)
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
