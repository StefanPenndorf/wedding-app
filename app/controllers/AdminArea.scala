package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._
import model.Benutzer

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object AdminArea extends Controller with AuthElement with WeddingAuthConfig {

  def gaesteliste = Action { implicit request =>
    Ok(views.html.gaesteliste(Benutzer.alleBenutzer()))
  }

  def gastFreischalten(id: Long): Action[AnyContent] = Action { implicit request =>
    Benutzer.findeMitId(id) match {
      case Some(gast) => gastFreischalten(gast)
      case None       => gastUnbekannt(request)
    }
  }

  private def gastFreischalten(benutzer: Benutzer) = {
    benutzer.freischalten()

    Redirect(routes.AdminArea.gaesteliste())
      .flashing("erfolgsMeldung"  -> (benutzer.name.vorname + " wurde freigeschaltet"))
  }

  private def gastUnbekannt(implicit request: Request[AnyContent]) = {
    NotFound(views.html.gaesteliste(Benutzer.alleBenutzer()))
  }
}
