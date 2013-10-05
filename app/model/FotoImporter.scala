package model

import com.google.inject._
import java.io.File

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait FotoImporter {

  def importiere(bild: File, besitzer: Benutzer)

}

@Singleton
class FotoImporterImpl extends FotoImporter {
  def importiere(bild: File, besitzer: Benutzer) {

    Foto.speichereFoto(new Array[Byte](2), besitzer)
  }
}
