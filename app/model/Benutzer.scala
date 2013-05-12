package model

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import scala.util.Random

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
case class PlainTextPasswort(passwort: String)
object PlainTextPasswort {
  def generate = {
    val length = 12
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') ++ ("-!")
    val randomPasswort = (1 to length).map(
      x => chars(Random.nextInt(chars.length))
    ).mkString("")
    PlainTextPasswort(randomPasswort)
  }


}

case class Benutzer(
    id: Pk[Long] = NotAssigned,
    name: BenutzerName,
    email: EMail,
    passwort: Option[String] = None) {

  def this(vorname: String, nachname: String, email: String) = this(NotAssigned, BenutzerName(vorname,nachname), email)

  def freischalten() {
    Benutzer.freischalten(this)
  }
}

trait BenutzerRepository {
  def alleBenutzer(): List[Benutzer]
  def findeMitId(id: Long): Option[Benutzer]
}

class BenutzerDatabaseRepository extends BenutzerRepository {
  def alleBenutzer() = Benutzer.alleBenutzer()
  def findeMitId(id: Long) = Benutzer.findeMitId(id)
}

object Benutzer {

  def freischalten(bewerber: Benutzer): PlainTextPasswort = {


    DB.withConnection { implicit connection =>

    }

    //BCrypt.hashpw(pw, BCrypt.gensalt())

    //findeMitId(bewerber.id.get).get
    null
  }


  def alleBenutzer(): List[Benutzer] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users
        """
      ).as(simple *)
    }
  }

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


  def findeMitId(id: Long): Option[Benutzer] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM users WHERE id={id}
        """
      ).on(
        'id -> id
      ).as(simple.singleOpt)
    }
  }
}
