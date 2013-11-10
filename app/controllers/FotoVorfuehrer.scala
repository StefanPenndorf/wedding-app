package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthenticationElement
import model._
import com.google.inject._
import play.api.libs.iteratee.Enumerator
import play.api.Logger
import scala.Some
import model.Gueltig
import play.api.mvc.SimpleResult
import model.BenutzerName
import model.BilderMitFehlern
import play.api.mvc.ResponseHeader
import model.AlleBilderGueltig
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import viewmodel.FotoalbumSeite

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

  private val log = Logger("hochladen")

  def fotoalben = StackAction{ implicit request =>
    Ok(views.html.fotoalben(verwalter.alleFotoalben()))
  }

  def fotoalbum(besitzerName: BenutzerName, seite: Int): Action[AnyContent] =  StackAction{ implicit request =>
    val findeFotoSeite = scala.concurrent.Future {
      gästeliste.findeGastMitName(besitzerName)
    }.map {
      case Some(besitzer) => besitzer.fotoalbum
      case None => None
    }

    Async {
      findeFotoSeite.map {
        case Some(fotoalbum) => Ok(views.html.fotoalbumSeite(FotoalbumSeite(fotoalbum, seite)))
        case None => NotFound("Ungültiges Album")
      }
    }
  }

  def fotoalbumEinzelfoto(besitzerName: BenutzerName, fotoPosition: Long = 1): Action[AnyContent] =  StackAction{ implicit request =>
    val findFotoalbumTask = scala.concurrent.Future {
      gästeliste.findeGastMitName(besitzerName)
    }.map {
      case Some(besitzer) => besitzer.fotoalbum
      case None => None
    }.map {
      case Some(album) => album.fotoMitPosition(fotoPosition).map{f => (album, f)}
      case None => None
    }

    Async {
      findFotoalbumTask.map {
        case Some((album, foto)) => Ok(views.html.foto(album, foto))
        case None => NotFound("Ungültiges Album")
      }
    }
  }

  def foto(id: Long) = StackAction { implicit request =>
    val loadFotoFuture = scala.concurrent.Future { Foto.findeMitId(id) }
    Async{
      loadFotoFuture.map {
        case Some(foto) => sendFotoResult(foto)
        case None => NotFound("Angefordertes Foto nicht gefunden!")
      }
    }
  }

  private def sendFotoResult(foto: Foto): Result = {
    val fileContent: Enumerator[Array[Byte]] = Enumerator(foto.content)
    SimpleResult(
      header = ResponseHeader(200, Map(
        "Content-Type" -> foto.mimeType
      )),
      body = fileContent
    )
  }

  def hochladen = StackAction(parse.multipartFormData){ implicit request =>
    val currentUser = loggedIn
    log.info(s"Hochladen gestartet durch $currentUser")

    Async {
      scala.concurrent.Future {

        val pruefergebnisse = request.body.files.map { filePart =>
          BildDateiPruefer.pruefeBild(filePart)
        }

        val ungueltige = pruefergebnisse collect { case g:Fehler => g }

        if(!ungueltige.isEmpty) {
          BilderMitFehlern(ungueltige)
        } else {
          AlleBilderGueltig(pruefergebnisse collect {
            case g: Gueltig => g
          })
        }
      }.map {
        case AlleBilderGueltig(gueltige) => {
            log.info("start import map")
            val importTasks = gueltige.map { g =>
              val p = g.filePart
              log.info("mapping to import jobs")
              scala.concurrent.Future{
                log.info(s"import file <${p.filename}>")
                fotoImporter.importiere(p.ref.file, currentUser)
                log.info(s"import file <${p.filename}> complete")
                p
              }
            }

            Left(scala.concurrent.Future.fold(importTasks)(0){ ( sum, filePart) =>
              log.info(s"fold file import <${filePart.filename}>")
              sum + 1
            })
        }
        case BilderMitFehlern(ungueltige) => Right(BilderMitFehlern(ungueltige))
      }.map {
        case Left(promise) => {
          Await.result(promise, Duration(10, TimeUnit.MINUTES)) match {
            case 0 =>   Redirect(routes.FotoVorfuehrer.fotoalben()).flashing(
              "fehlerMeldung" -> "Es wurde keine Datei importiert. Keine Datei ausgewählt?")
            case sum => Redirect(routes.FotoVorfuehrer.fotoalben()).flashing(
              "erfolgsMeldung" -> s"$sum Bild(er) erfolgreich zu deinem Album hinzugefügt.")
          }
        }
        case Right(BilderMitFehlern(ungueltige)) => {
            Redirect(routes.FotoVorfuehrer.fotoalben()).flashing(
              "fehlerMeldung" -> "Mindestens eine Datei konnte nicht importiert werden, weil sie kein Bild oder zu groß war (max. 8 MB).")
        }
      }
    }
  }

}
