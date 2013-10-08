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
class FotoImporterImpl @Inject()(
      verwalter: FotoalbenVerwalter
) extends FotoImporter {
  def importiere(bild: File, besitzer: Benutzer) {
    val fotoalbum = verwalter.findeFotoalbumVon(besitzer).getOrElse(Fotoalbum(besitzer, 0))

    verwalter.speichereFoto(new Array[Byte](2), fotoalbum)
  }
}
