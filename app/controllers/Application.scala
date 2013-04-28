package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Application extends Controller {

  val loginFormular = Form(
    mapping(
       "loginname" -> text(),
       "passwort" -> text()
    )
    ((loginname, passwort) => "" )
    ((login: String) => Some("",""))
    .verifying("login.error", login => false)
  )


  def index = Action {
    Ok(views.html.index(loginFormular))
  }

  def impressum = Action {
    Ok(views.html.impressum())
  }

  def login = Action { implicit request =>
    loginFormular.bindFromRequest.fold(
      errors => BadRequest(views.html.index(errors)),

      login => doLogin(login)
    )
  }

  private def doLogin(login: String) = {
    Redirect("http://www.google.de")
  }

}