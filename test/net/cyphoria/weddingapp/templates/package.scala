package net.cyphoria.weddingapp

import _root_.model.{BenutzerName, Benutzer}
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
package object templates {

  val IGNORED = None

  val TERESA = Benutzer(id=Id(1), name = BenutzerName("Teresa", "Merfert"), email = "teresa@cyphoria.net", passwort = IGNORED)
  val STEPHANIE = Benutzer(id=Id(2), name = BenutzerName("Stephanie", "Geiler"), email = "steffi@cyphoria.net", passwort = IGNORED)
  val KERSTIN = Benutzer(id=Id(3), name = BenutzerName("Kerstin", "Albert"), email = "kerstin@cyphoria.net", passwort = IGNORED)

}
