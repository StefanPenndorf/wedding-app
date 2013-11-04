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

  def fotoMitPosition(position: Long): Option[Foto] = {
    if(position < 1) {
      None
    } else {
      DB.withConnection { implicit connection =>
        SQL(
          """
            SELECT id,besitzer,foto,position FROM fotos f
            WHERE f.besitzer = {besitzerId} AND
                  f.position = {position}
          """
        ).on(
          'besitzerId -> besitzer.id,
          'position -> position
        ).as(Foto.simple.singleOpt)
      }
    }
  }


  def naechstePosition(foto: Foto): Option[Long] = {
    val nextPos = foto.position + 1
    fotoMitPosition(nextPos).map( _=> nextPos)
  }

  def vorhergehendePosition(foto: Foto): Option[Long] = {
    val prevPos = foto.position - 1
    fotoMitPosition(prevPos).map( _=> prevPos)
  }

  def alleFotos: Seq[model.Foto] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
            SELECT id,besitzer,foto,position FROM fotos f
            WHERE f.besitzer = {besitzerId}
            LIMIT 45
        """
      ).on(
        'besitzerId -> besitzer.id
      ).as(Foto.simple +)
    }
  }

}

object Fotoalbum {

  def findeFotoalbumVon(besitzer: Benutzer): Option[Fotoalbum] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT COUNT(f.id) as anzahlFotos FROM fotos f
          WHERE f.besitzer = {besitzerId}
          HAVING COUNT(f.id) > 0
        """
      ).on(
        'besitzerId -> besitzer.id
      ).as(long("anzahlFotos").singleOpt) map {
        case anzahlFotos => Fotoalbum(besitzer, anzahlFotos)
      }
    }
  }


}

trait FotoalbenVerwalter {
  def alleFotoalben(): List[Fotoalbum]
  def speichereFotoFuerBenutzer(foto: Array[Byte], besitzer: Benutzer)
}

class PersistenterFotoalbenVerwalter extends FotoalbenVerwalter {

  def alleFotoalben(): List[Fotoalbum] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT COUNT(f.id) as anzahlFotos, u.* FROM fotos f
          LEFT JOIN users u ON f.besitzer = u.id
          GROUP BY u.id
        """
      ).as(Benutzer.simple ~ long("anzahlFotos") *) map {
          case besitzer~anzahlFotos => Fotoalbum(besitzer, anzahlFotos)
      }
    }
  }

  def speichereFotoFuerBenutzer(foto: Array[Byte], besitzer: Benutzer) {
    val insertedId = DB.withTransaction { implicit connection =>
      val pos = SQL(
        """SELECT COUNT(*)+1 FROM
              ( SELECT id FROM fotos f
                WHERE f.besitzer={besitzerid}
                ORDER BY f.id
                FOR UPDATE ) r """
      ).on(
        'besitzerid -> besitzer.id
      ).as(scalar[Long].single)

      val insertId = SQL(
        """
          INSERT INTO fotos (besitzer, foto, position) VALUES
            ( {besitzerid}, {foto}, {pos} )
        """
      ).on(
        'besitzerid -> besitzer.id,
        'foto -> new Array[Byte](2),
        'pos -> pos
      ).executeInsert()

      insertId
    }

    DB.withConnection { implicit connection =>
      SQL(
        """
            UPDATE fotos SET foto = {foto} WHERE id = {id}
        """
        ).on(
          'id -> insertedId,
          'foto -> foto
        ).executeUpdate()
    }
  }

}