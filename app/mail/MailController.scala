package mail

import model.Benutzer
import com.typesafe.plugin._
import play.api.Play.current

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait MailController {

  def sendeFreischaltungsbenachrichtigung(benutzer: Benutzer)

}

/**
 * TODO Diese Klasse ist nicht getestet
 */
class TemplateMailController extends MailController {

  def sendeFreischaltungsbenachrichtigung(benutzer: Benutzer) {
     createMail
       .addRecipient(benutzer.email.email)
       .setSubject("Du wurdest als Hochzeitsgast freigeschaltet")
       .send(views.txt.mail.freischalteBenachrichtigung(benutzer).toString())
  }

  private[TemplateMailController] def createMail: MailerAPI = {
    val mail = use[MailerPlugin].email
    mail.addFrom("Steffi und Stefan <hochzeit@cyphoria.net>")
  }

}
