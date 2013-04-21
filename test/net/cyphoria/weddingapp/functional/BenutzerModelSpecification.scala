package net.cyphoria.weddingapp.functional

import model._
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test._
import play.api.Play.current
import play.api.db._
import anorm.SqlParser._

import anorm._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class BenutzerModelSpecification extends Specification {

  def laufenderAnwendung[T](block: => T) = running(FakeApplication(additionalConfiguration = inMemoryDatabase()))(block)

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
