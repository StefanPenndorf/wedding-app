package controllers

import play.api.mvc._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def impressum = Action {
    Ok(views.html.impressum())
  }


}