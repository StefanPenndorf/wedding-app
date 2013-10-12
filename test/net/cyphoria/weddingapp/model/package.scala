package net.cyphoria.weddingapp

import _root_.model.{BenutzerName, Benutzer}
import net.cyphoria.weddingapp.functional._
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
package object model {

  // TODO Refactor tests so dass Konstanten aus model benutzt werden.
  val KERSTIN = new Benutzer(Id(1L), BenutzerName("Kerstin", "Albert"), "kerstin@cyphoria.net", Some("$2a$10$k5TmtHnitQvFCNAp8SbuFeq1VlhlcSGkXl6JAcwZFX20mRZKgEgm."))

  val PNG_IMAGE_CONTENT: Array[Byte] = "\211PNG\u000D\u000A\u001A\u000AABCDE".toCharArray.map(_.toByte)
  val JPEG_IMAGE_CONTENT: Array[Byte] = "\u00FF\u00D8\u00FFABCDE".toCharArray.map(_.toByte)


  def mitDatenbank[T](block: => T) = laufenderAnwendung(block)

  def DatenbankMit[T](fixtureFileName: String)(block: => T) = laufenderAnwendungMitScenario(fixtureFileName)(block)

}
