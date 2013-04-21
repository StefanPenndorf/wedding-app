package model

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class HochzeitsGastBewerbung(vorname: String, nachname: String, email: String) {
  def benutzerName = BenutzerName(vorname, nachname)

}
