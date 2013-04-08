package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import registrierung.HochzeitsGastBewerbung

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Registrierung extends Controller {

  val registrierungsFormular = Form(
    mapping(
        "nutzername" -> text(minLength = 3, maxLength = 50),
        "email" -> email
    )(HochzeitsGastBewerbung.apply)(HochzeitsGastBewerbung.unapply)
  )

  def registrieren = TODO

  def formularAnzeigen = Action {
    Ok(views.html.registrieren(registrierungsFormular))
  }



}
