package model

import anorm._
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
