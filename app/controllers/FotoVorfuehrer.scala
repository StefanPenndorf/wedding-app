package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.AuthenticationElement

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object FotoVorfuehrer extends Controller with AuthenticationElement with WeddingAuthConfig  {

  def fotoalben = StackAction{ implicit request =>
    Ok(views.html.fotoalben())
  }

  def hochladen = StackAction(parse.multipartFormData){ implicit request =>
    request.body.file("bilddatei").map { picture =>
//      import java.io.File
      val filename = picture.filename
      picture.ref
//      val contentType = picture.contentType
//      picture.ref.moveTo(new File("/tmp/picture"))
      Redirect(routes.FotoVorfuehrer.fotoalben()).flashing(
        "erfolgsMeldung" -> "Bild erfolgreich zu deinem Album hinzugefügt."
      )
    }.getOrElse {
      Redirect(routes.FotoVorfuehrer.fotoalben()).flashing(
        "fehlerMeldung" -> "Keine Datei ausgewählt."
      )
    }
  }


}
