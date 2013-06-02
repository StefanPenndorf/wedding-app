package model

import com.google.inject._
import mail.MailController

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Hochzeitsplaner {

  def gastFreischalten(id: Long): Option[Benutzer]

}

@Singleton
class HochzeitsplanerImpl @Inject()(
                                     benutzerRepository: Gästeliste,
                                     mailController: MailController
                                     ) extends Hochzeitsplaner {
  def gastFreischalten(id: Long): Option[Benutzer] = {
    benutzerRepository.findeGastMitId(id) match {
      case Some(bewerber) => gastFreischalten(bewerber)
      case None       => None
    }
  }

  private def gastFreischalten(bewerber: Benutzer) = {
    val passwort = bewerber.freischalten()

    mailController.sendeFreischaltungsbenachrichtigung(bewerber, passwort)

    Some(bewerber)
  }
}
