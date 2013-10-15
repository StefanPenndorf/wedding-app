package model

import com.google.inject._
import java.io.File
import scalax.io._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait FotoImporter {

  def importiere(bild: File, besitzer: Benutzer)

}

@Singleton
class FotoImporterImpl @Inject()(
      verwalter: FotoalbenVerwalter
) extends FotoImporter {
  def importiere(bild: File, besitzer: Benutzer) {
    verwalter.speichereFotoFuerBenutzer(Resource.fromFile(bild).byteArray, besitzer)
  }
}
