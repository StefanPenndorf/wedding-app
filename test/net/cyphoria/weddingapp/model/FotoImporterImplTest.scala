package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import org.scalamock.specs2.MockFactory
import model.{PersistenteGästeliste, FotoImporterImpl}
import java.io.File

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoImporterImplTest extends Specification with MockFactory {

  val einGast = KERSTIN

  "FotoImporter" should {
    "ein neues Album anlegen, wenn ein Foto hochgeladen wird" in DatenbankMit("einemGast") {
      val gästeliste = new PersistenteGästeliste()
      val tempfile = new File("/tmp/doesnotexist")
      val importer: FotoImporterImpl = new FotoImporterImpl()

      importer.importiere(tempfile, einGast)
      gästeliste.gästeMitFotoalbum must contain(einGast)
    }

  }


}
