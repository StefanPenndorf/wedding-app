package model

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class Foto(id: Pk[Long] = NotAssigned) {

}


object Foto {

  def speichereFoto(foto: Array[Byte], besitzer: Benutzer) {
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