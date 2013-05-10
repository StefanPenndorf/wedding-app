package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._
import model.{BenutzerRepository, Benutzer}
import com.google.inject._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
@Singleton
class AdminArea @Inject()(benutzerRepository: BenutzerRepository) extends Controller with AuthElement with WeddingAuthConfig {

  def gaesteliste = Action { implicit request =>
    Ok(zeigeGaesteliste)
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
    NotFound(zeigeGaesteliste)
  }

  private def zeigeGaesteliste(implicit request: Request[AnyContent]) =
    views.html.gaesteliste(benutzerRepository.alleBenutzer())
}
