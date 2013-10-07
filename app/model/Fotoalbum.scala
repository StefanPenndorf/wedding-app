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

object Fotoalbum {

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

}