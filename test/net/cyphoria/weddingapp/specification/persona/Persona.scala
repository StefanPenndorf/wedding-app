package net.cyphoria.weddingapp.specification.persona

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class Persona(email: String, passwort:String, vorname: String, nachname: String) {
  def mitPasswort(newPasswort: String) = Persona(email, newPasswort, vorname, nachname)
}

object Persona {
  val Stefan = Persona("stefan@cyphoria.net", "ichHeirate", "Stefan", "Penndorf")
  val Kerstin = Persona("kerstin@cyphoria.net", "heiraten", "Kerstin", "Albert")
}
