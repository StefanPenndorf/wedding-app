package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import registrierung.HochzeitsGastBewerbung
import play.api.db.DB

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Registrierung extends Controller {

  val registrierungsFormular = Form(
    mapping(
        "nutzername" -> text(minLength = 3, maxLength = 50)
                        .verifying(pattern("""[a-zA-Z0-9]*""".r, "nutzername.pattern")),
        "email" -> email,
        "sicherheitsfrage" -> text(maxLength=150)
                              .verifying("sicherheitsfrage.error", antwort => antwort.toLowerCase == "albert schweizer")
    )((name, email, _) => HochzeitsGastBewerbung(name, email))
     ((bewerbung: HochzeitsGastBewerbung) => Some(bewerbung.nutzername, bewerbung.email, ""))
     .verifying("nutzername.error.benutzt", { bewerbung =>
         false
    })
  )

  def registrieren = Action { implicit request =>
    registrierungsFormular.bindFromRequest.fold(
      errors => BadRequest(views.html.registrieren(errors)),

      bewerbung => nimmRegistrierungVor(bewerbung)
    )
  }

  private def nimmRegistrierungVor(bewerbung: HochzeitsGastBewerbung) = {
    import play.api.Play.current
    DB.withConnection { implicit  c =>

    }



    Redirect(routes.Registrierung.registrierungsBestaetigung())
            .flashing("name"  -> bewerbung.nutzername,
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
        views.html.registrierungsBestaetigung(HochzeitsGastBewerbung(name, email))
      }
    }
  }



}
