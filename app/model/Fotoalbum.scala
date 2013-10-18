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

  def fotoMitPosition(nummer: Long): Option[Foto] = {
    if(nummer < 1) {
      None
    } else {
      DB.withConnection { implicit connection =>
        SQL(
          """
            SELECT id,besitzer,foto FROM fotos f
            WHERE f.besitzer = {besitzerId}
            ORDER BY ID ASC
            LIMIT {idx},1
          """
        ).on(
          'besitzerId -> besitzer.id,
          'idx -> (nummer - 1)
        ).as(Foto.simple.singleOpt)
      }
    }
  }


  def naechstePosition(foto: Foto): Option[Long] = {
    val nextPos = positionVon(foto) + 1
    fotoMitPosition(nextPos).map( _=> nextPos)
  }

  private def positionVon(foto: Foto): Long = {
    DB.withConnection { implicit connection =>
      SQL(
        """
            SELECT COUNT(*)+1 as cnt FROM fotos f
            WHERE f.besitzer = {besitzerId} AND
                  f.id < {fotoId}
        """
      ).on(
        'besitzerId -> besitzer.id,
        'fotoId -> foto.id.get
      ).as(scalar[Long].single)
    }
  }

  lazy val erstesFoto: Foto = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT id,besitzer,foto FROM fotos f
          WHERE f.besitzer = {besitzerId}
          ORDER BY ID ASC
          LIMIT 1
        """
      ).on(
        'besitzerId -> besitzer.id
      ).as(Foto.simple.single)
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
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into fotos
          (besitzer,   foto) values
          ({besitzerid}, {foto})
        """
      ).on(
        'besitzerid -> besitzer.id,
        'foto -> foto
      ).executeUpdate()
    }
  }

}