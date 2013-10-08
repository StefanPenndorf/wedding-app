package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthenticationElement
import java.io.File
import model._
import com.google.inject._
import model.BenutzerName
import scala.Some

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

  def fotoalbum(besitzerName: BenutzerName): Action[AnyContent] =  StackAction{ implicit request =>
    gästeliste.findeGastMitName(besitzerName) match {
      case Some(besitzer) => fotoalbum(besitzer)
      case None => NotFound("Ungültiges Album")
    }
  }

  private def fotoalbum(albumBesitzer: Benutzer)(implicit flash: Flash): Result  = {
    verwalter.findeFotoalbumVon(albumBesitzer) match {
      case Some(album) => Ok(views.html.foto(album))
      case None => NotFound("Ungültiges Album")
    }
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
