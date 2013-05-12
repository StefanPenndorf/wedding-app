package model

import play.api.db.DB
import anorm._
import play.api.Play.current

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class Registrand(vorname: String, nachname: String, email: String) {
  def benutzerName = BenutzerName(vorname, nachname)
  def registrieren() {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into users
          ( email,   vorname,   nachname) values
          ({email}, {vorname}, {nachname})
        """
      ).on(
        'email -> email,
        'vorname -> vorname,
        'nachname -> nachname
      ).executeUpdate()
    }
  }
}

