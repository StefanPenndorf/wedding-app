package net.cyphoria.weddingapp.specification.persona

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class Persona(email: String, passwort:String, vorname: String, nachname: String) {
  def mitPasswort(newPasswort: String) = Persona(email, newPasswort, vorname, nachname)
}

object Persona {
  def fromName(personaName: String): Persona = personaName match {
    case "Stefan" => Stefan
    case "Kerstin" => Kerstin
  }

  val Stefan = Persona("stefan@cyphoria.net", "ichHeirate", "Stefan", "Penndorf")
  val Kerstin = Persona("kerstin@cyphoria.net", "heiraten", "Kerstin", "Albert")
}
