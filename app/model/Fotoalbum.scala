package model

import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.api.Play.current


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class Fotoalbum(
    besitzer: Benutzer,
    anzahlFotos: Long
                      ) {

}
        // TODO CLEAN UP ALL OTHER STUFF USING GÄSTELISTE TO USE FOTOALBUM

trait FotoalbenVerwalter {
  def alleFotoalben(): List[Fotoalbum]
  def findeFotoalbumVon(besitzer: Benutzer): Option[Fotoalbum]
  def speichereFoto(foto: Array[Byte], fotoalbum: Fotoalbum)
}

class PersistenterFotoalbenVerwalter extends FotoalbenVerwalter {

  def alleFotoalben(): List[Fotoalbum] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT COUNT(u.id) as anzahlFotos, u.* FROM fotos f
          LEFT JOIN users u ON f.besitzer = u.id
          GROUP BY u.id
        """
      ).as(Benutzer.simple ~ long("anzahlFotos") *) map {
          case besitzer~anzahlFotos => Fotoalbum(besitzer, anzahlFotos)
      }
    }
  }

  def speichereFoto(foto: Array[Byte], fotoalbum: Fotoalbum) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into fotos
          (besitzer,   foto) values
          ({besitzerid}, {foto})
        """
      ).on(
        'besitzerid -> fotoalbum.besitzer.id,
        'foto -> foto
      ).executeUpdate()
    }
  }

  def findeFotoalbumVon(besitzer: Benutzer): Option[Fotoalbum] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT COUNT(u.id) as anzahlFotos, u.* FROM fotos f
          LEFT JOIN users u ON f.besitzer = u.id
          GROUP BY u.id
        """
      ).as((Benutzer.simple ~ long("anzahlFotos")).singleOpt) map {
        case `besitzer`~anzahlFotos => Fotoalbum(besitzer, anzahlFotos)
      }
    }
  }
}