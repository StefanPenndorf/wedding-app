package model

import play.api.db.DB
import anorm._
import play.api.Play.current

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Gästeliste {
  def gäste: List[Benutzer]
  def vip: List[Benutzer]
  def admins: List[Benutzer]
  def gästeMitFotoalbum: Set[Benutzer]
  def findeGastMitName(name: BenutzerName): Option[Benutzer]
  def findeGastMitId(id: Long): Option[Benutzer]
  def findeGastMitEMail(email: EMail): Option[Benutzer]
}


class PersistenteGästeliste extends Gästeliste {

  def findeGastMitName(name: BenutzerName) =  {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE vorname={vorname} AND nachname={nachname}
        """
      ).on(
        'vorname -> name.vorname,
        'nachname -> name.nachname
      ).as(Benutzer.simple.singleOpt)
    }
  }


  def findeGastMitId(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE id={id}
        """
      ).on(
        'id -> id
      ).as(Benutzer.simple.singleOpt)
    }
  }

  def gäste = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users
        """
      ).as(Benutzer.simple *)
    }
  }

  def vip = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE passwort IS NOT NULL
        """
      ).as(Benutzer.simple *)
    }
  }

  def findeGastMitEMail(email: EMail) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE email={email}
        """
      ).on(
        'email -> email.email
      ).as(Benutzer.simple.singleOpt)
    }
  }

  def admins: List[Benutzer] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE istAdmin = TRUE
        """
      ).as(Benutzer.simple *)
    }
  }

  def gästeMitFotoalbum: Set[Benutzer] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT u.* FROM users u
          WHERE EXISTS (SELECT * FROM fotos f WHERE f.besitzer = u.id)
          GROUP BY u.id
        """
      ).as(Benutzer.simple *).to[Set]
    }
  }
}
