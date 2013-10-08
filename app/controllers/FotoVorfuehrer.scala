package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.AuthenticationElement
import java.io.File
import model.{FotoalbenVerwalter, FotoImporter, Gästeliste}
import com.google.inject._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
@Singleton
class FotoVorfuehrer @Inject()(
                                gästeliste: Gästeliste,
                                fotoImporter: FotoImporter,
                                verwalter: FotoalbenVerwalter
                              ) extends Controller with AuthenticationElement with WeddingAuthConfig  {

  def fotoalben = StackAction{ implicit request =>
    Ok(views.html.fotoalben(verwalter.alleFotoalben()))
  }

  def hochladen = StackAction(parse.multipartFormData){ implicit request =>
    val currentUser = loggedIn
    request.body.file("bilddatei").map { picture =>
        val tempfile = File.createTempFile("pic", "png")
        try {
          picture.ref.moveTo(tempfile, replace = true)
          fotoImporter.importiere(tempfile, currentUser)
        } finally {
          tempfile.delete()
        }

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
