package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._
import model.{Hochzeitsplaner, Gästeliste, Benutzer}
import com.google.inject._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
@Singleton
class AdminArea @Inject()(
                           gästeliste: Gästeliste,
                           hochzeitsplaner: Hochzeitsplaner
                           ) extends Controller with AuthElement with WeddingAuthConfig {

  def gaesteliste =  StackAction(AuthorityKey -> AdminBerechtigung) { implicit request =>
    Ok(zeigeGaesteliste)
  }

  def gastFreischalten(id: Long): Action[AnyContent] = StackAction(AuthorityKey -> AdminBerechtigung) { implicit request =>
    hochzeitsplaner.gastFreischalten(id) match {
      case Some(gast) => gastFreigeschaltet(gast)
      case None       => gastUnbekannt(request)
    }
  }

  private def gastFreigeschaltet(bewerber: Benutzer) = {
    Redirect(routes.AdminArea.gaesteliste())
      .flashing("erfolgsMeldung"  -> (bewerber.name.vorname + " wurde freigeschaltet"))
  }

  private def gastUnbekannt(implicit request: Request[AnyContent]) = {
    NotFound(zeigeGaesteliste)
  }

  private def zeigeGaesteliste(implicit request: Request[AnyContent]) =
    views.html.gaesteliste(gästeliste.gäste)
}
