package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import org.specs2.mock._
import model._
import org.specs2.mock.mockito.ArgThat
import org.specs2.matcher.{MatchResult, Expectable, Matcher}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoImporterImplTest extends Specification with Mockito with ArgThat {

  val einGast = KERSTIN

  "FotoImporter" should {
    "ein neues Foto zum Album des Benutzers hinzufügen, wenn ein Foto hochgeladen wird" in DatenbankMit("einemGast") {
      val verwalter = mock[FotoalbenVerwalter]
      val importer: FotoImporterImpl = new FotoImporterImpl(verwalter)

      importer.importiere(JPEG_BILD_DATEI, einGast)

      there was one(verwalter).speichereFotoFuerBenutzer(any[Array[Byte]], argThat(===(einGast)))
    }

    "den Inhalt des Foto speichern, wenn ein Foto hochgeladen wird" in DatenbankMit("einemGast") {
      val verwalter = mock[FotoalbenVerwalter]
      val importer: FotoImporterImpl = new FotoImporterImpl(verwalter)

      importer.importiere(JPEG_BILD_DATEI, einGast)

      there was one(verwalter).speichereFotoFuerBenutzer(arrayStartsWithByteSequence(JPEG_IMAGE_CONTENT), ===(einGast))
    }

    def arrayStartsWithByteSequence(head: Array[Byte]) = new ArrayStartWithByteSequence(head)

    class ArrayStartWithByteSequence(val head: Array[Byte]) extends Matcher[Array[Byte]] {
      val HEAD_STRING = toString(head)
      def apply[S <: Array[Byte]](s: Expectable[S]): MatchResult[S] = {
        result(
          s.value.startsWith(head),
          f"Array[Byte] start with $HEAD_STRING%s",
          f"expected Array[Byte] start with $HEAD_STRING%s but found " + toString(s.value),
           s)
      }

      private def toString(arr: Array[Byte]) = arr.take(head.length + 3).map("%02X" format _).mkString
    }

  }

}
