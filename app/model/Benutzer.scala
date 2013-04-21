package model

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class BenutzerName(vorname: String, nachname: String)
case class EMail(email: String)

case class Benutzer(
    id: Pk[Long] = NotAssigned,
    name: BenutzerName,
    email: EMail) {
}

object Benutzer {

  def existiertMitName(name: BenutzerName): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT count(id) FROM users WHERE vorname={vorname} AND nachname={nachname}
        """
      ).on(
        'vorname -> name.vorname,
        'nachname -> name.nachname
      ).as(scalar[Long].single) > 0
    }
  }


  def existiertMitEMail(email: String): Boolean = existiertMitEMail(EMail(email))


  def existiertMitEMail(email: EMail): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT count(id) FROM users WHERE email={email}
        """
      ).on(
        'email -> email.email
      ).as(scalar[Long].single) > 0
    }
  }

  def bewirbtSichMit(bewerbung: HochzeitsGastBewerbung) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into users
          ( email,   vorname,   nachname) values
          ({email}, {vorname}, {nachname})
        """
      ).on(
        'email -> bewerbung.email,
        'vorname -> bewerbung.vorname,
        'nachname -> bewerbung.nachname
      ).executeUpdate()
    }

  }

}
