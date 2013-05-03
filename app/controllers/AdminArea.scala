package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object AdminArea extends Controller with AuthElement with WeddingAuthConfig {

  def gaesteliste = Action {
    Ok(views.html.gaesteliste())
  }

}
