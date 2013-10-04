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

  def mitDatenbank[T](block: => T) = laufenderAnwendung(block)

  def DatenbankMit[T](fixtureFileName: String)(block: => T) = laufenderAnwendungMitScenario(fixtureFileName)(block)

}
