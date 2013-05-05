package net.cyphoria.weddingapp.functional

import model._
import org.specs2.mutable.Specification
import play.api.Play.current
import play.api.db._
import anorm.SqlParser._

import anorm._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class BenutzerModelSpecification extends Specification {

  def anzahlBewerber = {
     DB.withConnection { implicit connection =>
        SQL("select count(*) as cnt from users").as(scalar[Long].single)
     }
  }

  "Benutzer" should {
    "sich bewerben können" in {
      laufenderAnwendung {
          anzahlBewerber must equalTo(0)

          Benutzer.bewirbtSichMit(HochzeitsGastBewerbung("stefan", "penndorf", "stefan@cyphoria.net"))

          anzahlBewerber must equalTo(1)
      }
    }
  }
}
