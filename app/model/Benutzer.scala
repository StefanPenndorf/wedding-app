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
object EMail {
  implicit def toStringValue(mail: EMail): String = mail.email
  implicit def fromStringValue(email: String): EMail = EMail(email)
}

case class Benutzer(
    id: Pk[Long] = NotAssigned,
    name: BenutzerName,
    email: EMail,
    passwort: Option[String] = None) {

  def this(vorname: String, nachname: String, email: String) = this(NotAssigned, BenutzerName(vorname,nachname), email)

  def freischalten(): KlartextPasswort = {
    KlartextPasswort.generate
    //BCrypt.hashpw(pw, BCrypt.gensalt())
  }
}

object Benutzer {

  def authentifiziere(email: String, passwort: String): Option[Benutzer] = {
    findeMitEMail(email).filter { benutzer => benutzer.passwort.isDefined }
  }

  def findeMitEMail(email: EMail): Option[Benutzer] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE email={email}
        """
      ).on(
        'email -> email.email
      ).as(simple.singleOpt)
    }
  }


  val simple = {
      get[Long]("users.id") ~
      get[String]("users.email") ~
      get[String]("users.vorname") ~
      get[String]("users.nachname") ~
      get[Option[String]]("users.passwort") map {
      case id~email~vorname~nachname~passwort => Benutzer(Id(id), BenutzerName(vorname, nachname), EMail(email), passwort)
    }
  }
}
