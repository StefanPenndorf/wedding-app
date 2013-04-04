package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def impressum = Action {
    Ok(views.html.impressum())
  }

  
}