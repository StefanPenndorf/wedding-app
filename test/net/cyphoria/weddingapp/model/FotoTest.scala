package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model.Foto
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoTest extends Specification {

  val UNFUG_CONTENT: Array[Byte] = "\u00FF\u0034\u0056ABCDE".toCharArray.map(_.toByte)
  val OTHER_CONTENT: Array[Byte] = "\u0012\u0034\u0056ABCDE".toCharArray.map(_.toByte)

  val foto: Foto = Foto(Id(1), PNG_IMAGE_CONTENT, 1)

  "Foto" should {

    "kein Foto finden, wenn noch kein Foto hochgeladen wurde" in DatenbankMit("einemGast") {
      Foto.findeMitId(1L) must beNone
    }

    "ein Foto finden, wenn ein Foto hochgeladen wurde" in DatenbankMit("einemGastMitEinemFoto") {
      Foto.findeMitId(1L) must beSome(foto)
    }

    "den korrekten Mime-Type für PNG bestimmen" in {
      Foto(imageContent = PNG_IMAGE_CONTENT, position = 1).mimeType must beEqualTo("image/png")
    }

    "den korrekten Mime-Type für JPEG bestimmen" in {
      Foto(imageContent = JPEG_IMAGE_CONTENT, position = 1).mimeType must beEqualTo("image/jpeg")
    }

    "einen anderen Mime-Type für ungültige Daten bestimmen" in {
      Foto(imageContent = OTHER_CONTENT, position = 1).mimeType must not (beEqualTo("image/png") or beEqualTo("image/jpeg"))
    }

    "den Mime-Type 'application/octet-stream' bestimmen, wenn JMimeMagic keinen Treffer findet" in {
      Foto(imageContent = UNFUG_CONTENT, position = 1).mimeType must beEqualTo("application/octet-stream")
    }

  }

  }
