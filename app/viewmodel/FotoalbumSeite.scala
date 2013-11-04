package viewmodel

import model.Benutzer

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class FotoalbumSeite(besitzer: Benutzer,
                          fotos: Seq[model.Foto])

object FotoalbumSeite {
  def apply(album: model.Fotoalbum) = {
    new FotoalbumSeite(album.besitzer, album.alleFotos)
  }
}