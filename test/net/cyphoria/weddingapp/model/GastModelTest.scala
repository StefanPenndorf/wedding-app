package net.cyphoria.weddingapp.model

import org.specs2.mutable.Specification
import model._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class GastModelTest extends Specification {

  val einGast = KERSTIN
  val gästeliste = new PersistenteGästeliste

  "Ein Gast" should {

    "auf der Gästeliste stehen" in DatenbankMit("einemGast") {
        gästeliste.gäste must contain(einGast)
    }

    "auf der VIP-Liste stehen" in DatenbankMit("einemGast") {
        gästeliste.vip must contain(einGast)
    }

    "auf der Gästeliste unter der angegebenen E-Mail stehen" in DatenbankMit("einemGast") {
         val derGast = gästeliste.findeGastMitEMail(einGast.email).get

         derGast must_== einGast
    }

    "auf der Gästeliste unter dem angegebenen Namen stehen" in DatenbankMit("einemGast") {
      val derGast = gästeliste.findeGastMitName(einGast.name).get

      derGast must_== einGast
    }


    "auf der Gästeliste unter der angegebenen Id stehen" in DatenbankMit("einemGast") {
      val derGast = gästeliste.findeGastMitId(einGast.id.get).get

      derGast must_== einGast
    }

    "sich authentifizieren können" in DatenbankMit("einemGast") {
      Benutzer.authentifiziere(einGast.email, "ichHeirate") must beSome
    }

    "sich nicht authentifizieren können, wenn das Passwort falsch ist" in DatenbankMit("einemGast") {
      Benutzer.authentifiziere(einGast.email, passwort = "falsch") must beNone
    }

    "kein Fotoalbum haben, wenn er noch kein Foto hochgeladen hat" in DatenbankMit("einemGast") {
      einGast.fotoalbum must beNone
    }

    "ein Fotoalbum mit einem Foto haben, wenn er ein Foto hochgeladen hat" in DatenbankMit("einemGastMitEinemFoto") {
      einGast.fotoalbum must beSome(Fotoalbum(einGast, 1))
    }

    "ein Fotoalbum mit drei Fotos haben, wenn er drei Fotos hochgeladen hat" in DatenbankMit("einemGastMitDreiFotos") {
      einGast.fotoalbum must beSome(Fotoalbum(einGast, 3))
    }


  }



    "Ein nicht freigeschalteter Gast" should {

    val teresasEMail = "teresa@cyphoria.net"
    def nichtFreigeschalteterGast = gästeliste.findeGastMitEMail(teresasEMail).get

    "sich nicht authentifizieren können" in DatenbankMit("einemNichtFreigeschaltetenGast") {
      Benutzer.authentifiziere(teresasEMail, passwort = "irgendEins") must beNone
    }

    "nach dem Freischalten ein Passwort mit 12 Zeichen erhalten" in DatenbankMit("einemNichtFreigeschaltetenGast") {
      nichtFreigeschalteterGast.freischalten().passwort must have length(8)
    }

    "sich nach dem Freischalten mit dem neuen Passwort anmelden können" in DatenbankMit("einemNichtFreigeschaltetenGast") {
      val passwort = nichtFreigeschalteterGast.freischalten().passwort

      Benutzer.authentifiziere(teresasEMail, passwort) must beSome
    }

    "auf der Gästeliste stehen" in DatenbankMit("einemNichtFreigeschaltetenGast") {
        gästeliste.gäste must contain(nichtFreigeschalteterGast)
    }

    "nicht auf der VIP-Liste stehen" in DatenbankMit("einemNichtFreigeschaltetenGast") {
        gästeliste.vip must not(contain(nichtFreigeschalteterGast))
    }


    }

}
