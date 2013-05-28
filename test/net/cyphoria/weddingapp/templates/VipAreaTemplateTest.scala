package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeApplication
import model.{BenutzerName, Benutzer}
import anorm.Id

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class VipAreaTemplateTest extends Specification {

  val benutzerKerstin = Benutzer(id=Id(3), name = BenutzerName("Kerstin", "Albert"), email = "ignored", passwort = Some("ignored"))
  val vipArea = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.viparea(benutzerKerstin)
  }

  "Die Vip-Area" should {

      "eine persönliche Begrüßung enthalten" in {
        vipArea.body must contain("Willkommen Kerstin")
        vipArea.body must contain("""Auf dieser Seite findest du alle wichtigen Infos zu unserer Hochzeit.""")
      }
  }

}
