package net.cyphoria.weddingapp.specification.users

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class User(email: String, passwort:String)

object User {
  val Stefan = User("stefan@cyphoria.net", "ichHeirate")
  val Kerstin = User("kerstin@cyphoria.net", "p")
}
