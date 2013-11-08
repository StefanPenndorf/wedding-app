package viewmodel

import model.Fotoalbum

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class FotoalbumSeite(album: Fotoalbum,
                          seite: Int) {

  val besitzer = album.besitzer
  val fotos = album alleFotosAufSeite seite

}