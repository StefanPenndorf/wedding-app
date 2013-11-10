package model

import play.api.mvc.MultipartFormData
import play.api.libs.Files
import net.sf.jmimemagic.{MagicMatch, MagicMatchNotFoundException, Magic}
import play.api.Logger
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile
import java.io.File

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
sealed abstract class BildDateiPruefergebnis(val filename: String)
case class Gueltig(filePart: MultipartFormData.FilePart[Files.TemporaryFile]) extends BildDateiPruefergebnis(filePart.filename)
sealed abstract class Fehler(filename: String) extends BildDateiPruefergebnis(filename)
case class ZuGross(filePart: MultipartFormData.FilePart[Files.TemporaryFile]) extends Fehler(filePart.filename)
case class KeineBilddatei(filePart: MultipartFormData.FilePart[Files.TemporaryFile]) extends Fehler(filePart.filename)

sealed abstract class BilderBatchPruefergebnis()
case class AlleBilderGueltig(bilder: Seq[Gueltig])
case class BilderMitFehlern(fehler: Seq[Fehler])


object BildDateiPruefer {

  private val log = Logger("hochladen")

  private val MAXIMALE_DATEIGROESSE: Int = 8 * 1024 * 1024

  type Prueffunktion = FilePart[TemporaryFile] => BildDateiPruefergebnis

  def dateiGroessenPruefer: Prueffunktion = (filePart: FilePart[TemporaryFile]) => {
    if (filePart.ref.file.length() > MAXIMALE_DATEIGROESSE) {
      log.info(s"Datei ist zu groß <${filePart.filename}>")
      ZuGross(filePart)
    } else {
      Gueltig(filePart)
    }
  }

  def dateiErweiterungPruefer: Prueffunktion = (filePart: FilePart[TemporaryFile]) => {
      if (!filePart.filename.toLowerCase.endsWith(".png") && !filePart.filename.toLowerCase.endsWith(".jpg")) {
        log.info(s"Datei hat falsche Endung <${filePart.filename}>")
        KeineBilddatei(filePart)
      } else {
        Gueltig(filePart)
      }
  }

  def dateiInhaltPruefer: Prueffunktion = (filePart: FilePart[TemporaryFile]) => {
    tryMagicMatch(filePart.ref.file).map( result =>
      if(!result.getMimeType.startsWith("image")) {
        log.info(s"Datei hat falschen Typ <${filePart.filename}> <${Magic.getMagicMatch(filePart.ref.file, true).getMimeType}>")
        KeineBilddatei(filePart)
      } else {
        Gueltig(filePart)
      }
    ).getOrElse(
      KeineBilddatei(filePart)
    )
  }

  private def tryMagicMatch(file: File): Option[MagicMatch] = {
    try {
      Some(Magic.getMagicMatch(file, true))
    } catch {
      case e: MagicMatchNotFoundException => None
    }
  }


  def pruefeBild(filePart: MultipartFormData.FilePart[Files.TemporaryFile]): BildDateiPruefergebnis = {
    log.info(s"Prüfe Datei <${filePart.filename}>")

    Seq(dateiGroessenPruefer, dateiErweiterungPruefer, dateiInhaltPruefer).foldLeft(Gueltig(filePart).asInstanceOf[BildDateiPruefergebnis])(fuehrePruefungAus)
  }

  def fuehrePruefungAus: (BildDateiPruefergebnis, Prueffunktion) => BildDateiPruefergebnis = {
    (res, pruefer) => res match {
      case Gueltig(filePart) => pruefer(filePart)
      case _                 => res
    }
  }
}

