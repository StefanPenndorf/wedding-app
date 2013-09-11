package mail

import model.{KlartextPasswort, Benutzer}
import com.typesafe.plugin._
import play.api.Play.current

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait MailController {

  def sendeFreischaltungsbenachrichtigung(benutzer: Benutzer, passwort: KlartextPasswort)

  def sendeNewsletter(empfaenger: Benutzer)

}

/**
 * TODO Diese Klasse ist nicht getestet
 */
class TemplateMailController extends MailController {

  def sendeFreischaltungsbenachrichtigung(benutzer: Benutzer, passwort: KlartextPasswort) {
     createMail
       .addRecipient(benutzer.email.email)
       .setSubject("Du wurdest als Hochzeitsgast freigeschaltet")
       .send(views.txt.mail.freischalteBenachrichtigung(benutzer, passwort).toString())
  }

  def sendeNewsletter(empfänger: Benutzer) {
    createMail
      .addRecipient(empfänger.email.email)
      .setSubject("Neues von Steffi's und Stefans Hochzeit")
      .send(views.txt.mail.newsletter2(empfänger).toString())
  }

  private[TemplateMailController] def createMail: MailerAPI = {
    val mail = use[MailerPlugin].email
    mail.addFrom("Steffi und Stefan <hochzeit@cyphoria.net>")
  }
}
