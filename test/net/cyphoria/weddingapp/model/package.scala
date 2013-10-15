package net.cyphoria.weddingapp

import _root_.model.{BenutzerName, Benutzer}
import net.cyphoria.weddingapp.functional._
import anorm.Id
import org.springframework.core.io.ClassPathResource
import java.io.File

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
package object model {

  // TODO Refactor tests so dass Konstanten aus model benutzt werden.
  val KERSTIN = new Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))

  val PNG_IMAGE_CONTENT: Array[Byte] = "\211PNG\u000D\u000A\u001A\u000AABCDE".toCharArray.map(_.toByte)
  val JPEG_IMAGE_CONTENT: Array[Byte] = "\u00FF\u00D8\u00FF\u00E0\u0000\u0010\u004A\u0046\u0049\u0046".toCharArray.map(_.toByte)



  val JPEG_BILD_DATEI: File = new ClassPathResource("images/mara_und_lukas.jpg").getFile



  def mitDatenbank[T](block: => T) = laufenderAnwendung(block)

  def DatenbankMit[T](fixtureFileName: String)(block: => T) = laufenderAnwendungMitScenario(fixtureFileName)(block)

}
