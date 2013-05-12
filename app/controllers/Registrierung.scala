package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import model.{Bewerber, Benutzer, HochzeitsGastBewerbung}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Registrierung extends Controller {

  val NAMEN_CONSTRAINT = pattern("""[a-zA-ZöüäÖÜÄ-]*""".r, "name.pattern", "name.pattern.error")

  val registrierungsFormular = Form(
    mapping(
        "vorname"  -> text(minLength = 3, maxLength = 50)
                        .verifying(NAMEN_CONSTRAINT),
        "nachname" -> text(minLength = 3, maxLength = 50)
                        .verifying(NAMEN_CONSTRAINT),
        "email"    -> email,
        "sicherheitsfrage" -> text(maxLength=150)
                              .verifying("sicherheitsfrage.error", antwort => antwort.toLowerCase == "albert schweitzer")
    )
    (
        (vorname, nachname, email, _) => HochzeitsGastBewerbung(vorname, nachname, email)
    )
    (
        (bewerbung: HochzeitsGastBewerbung) => Some(bewerbung.vorname, bewerbung.nachname, bewerbung.email, "")
    ).verifying(
        "email.error.used", bewerbung => Benutzer.findeMitEMail(bewerbung.email).isEmpty
    ).verifying(
        "nutzername.error.used", bewerbung => !Benutzer.existiertMitName(bewerbung.benutzerName)
    )
  )

  def registrieren = Action { implicit request =>
    registrierungsFormular.bindFromRequest.fold(
      errors => BadRequest(views.html.registrieren(errors)),

      bewerbung => nimmRegistrierungVor(bewerbung)
    )
  }

  private def nimmRegistrierungVor(bewerbung: HochzeitsGastBewerbung) = {
    Bewerber bewirbtSichMit bewerbung

    import com.typesafe.plugin._
    import play.api.Play.current
    val mail = use[MailerPlugin].email
    mail.setSubject(bewerbung.vorname + " hat sich registriert")
    mail.addRecipient("stephaniegeiler@web.de", "stefan@cyphoria.net")
    mail.addFrom("Steffi und Stefan <hochzeit@cyphoria.net>")
    mail.send( bewerbung.toString )

    Redirect(routes.Registrierung.registrierungsBestaetigung())
            .flashing("name"  -> bewerbung.vorname,
                      "email" -> bewerbung.email)
  }

  def formular = Action {
    Ok(views.html.registrieren(registrierungsFormular))
  }

  def registrierungsBestaetigung = Action { implicit request =>
    if(flash.get("name").isEmpty) {
      NotFound
    } else {
      Ok {
        val name = flash.get("name").get
        val email = flash.get("email").get
        views.html.registrierungsBestaetigung(name, email)
      }
    }
  }



}
