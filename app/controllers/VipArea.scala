package controllers

import jp.t2v.lab.play2.auth.AuthenticationElement
import play.api.mvc._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object VipArea extends Controller with AuthenticationElement with WeddingAuthConfig {

  def viparea = StackAction{ implicit request =>
    Ok(views.html.viparea(loggedIn))
  }



}
