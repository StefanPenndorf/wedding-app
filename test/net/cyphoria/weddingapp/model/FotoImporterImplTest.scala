package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import org.scalamock.specs2.MockFactory
import model.{Fotoalbum, FotoalbenVerwalter, PersistenteGästeliste, FotoImporterImpl}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoImporterImplTest extends Specification with MockFactory {

  val einGast = KERSTIN

  "FotoImporter" should {
    val fotoalbum: Fotoalbum = Fotoalbum(einGast, 0)

    "ein neues Foto zum Album des Benutzers hinzufügen, wenn ein Foto hochgeladen wird" in DatenbankMit("einemGast") {
      val gästeliste = new PersistenteGästeliste()
      val verwalter = mock[FotoalbenVerwalter]
      val importer: FotoImporterImpl = new FotoImporterImpl(verwalter)
      (verwalter.findeFotoalbumVon _).expects(einGast).returns(Option(fotoalbum)).anyNumberOfTimes()
      (verwalter.speichereFoto _).expects(*, fotoalbum)

      importer.importiere(null, einGast)
    }

  }


}
