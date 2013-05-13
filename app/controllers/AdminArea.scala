package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._
import model.{Gästeliste, Benutzer}
import com.google.inject._
import mail.MailController

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
@Singleton
class AdminArea @Inject()(
                           benutzerRepository: Gästeliste,
                           mailController: MailController
                           ) extends Controller with AuthElement with WeddingAuthConfig {

  def gaesteliste = Action { implicit request =>
    Ok(zeigeGaesteliste)
  }

  def gastFreischalten(id: Long): Action[AnyContent] = Action { implicit request =>
    benutzerRepository.findeGastMitId(id) match {
      case Some(gast) => gastFreischalten(gast)
      case None       => gastUnbekannt(request)
    }
  }

  private def gastFreischalten(bewerber: Benutzer) = {
    val passwort = bewerber.freischalten()

    mailController.sendeFreischaltungsbenachrichtigung(bewerber, passwort)

    Redirect(routes.AdminArea.gaesteliste())
      .flashing("erfolgsMeldung"  -> (bewerber.name.vorname + " wurde freigeschaltet"))
  }

  private def gastUnbekannt(implicit request: Request[AnyContent]) = {
    NotFound(zeigeGaesteliste)
  }

  private def zeigeGaesteliste(implicit request: Request[AnyContent]) =
    views.html.gaesteliste(benutzerRepository.gäste)
}
