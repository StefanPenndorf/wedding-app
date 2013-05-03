package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import jp.t2v.lab.play2.auth.LoginLogout
import model.Benutzer


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Application extends Controller with LoginLogout with WeddingAuthConfig {

  val loginFormular = Form(
    mapping(
       "loginname" -> text(),
       "passwort" -> text()
    )
    ((loginname, passwort) => Benutzer.authentifiziere(loginname, passwort) )
    ((_.map(u => (u.email.email, ""))))
    .verifying("login.error", _.isDefined)
  )


  def index = Action {
    Ok(views.html.index(loginFormular))
  }

  def impressum = Action {
    Ok(views.html.impressum())
  }

  def authenticate = Action { implicit request =>
    loginFormular.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.index(formWithErrors)),
      user => gotoLoginSucceeded(user.get.id.get)
    )
  }

  private def authenticationSuccessfull(login: String) = {
    Redirect("http://www.google.de")
  }

}