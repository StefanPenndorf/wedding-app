package model

import com.google.inject._
import mail.MailController

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait NewsletterSender {
   def sendNewsletter()
   def testNewsletter()
}


@Singleton
class MailNewsletterSender  @Inject()(
                                 benutzerRepository: Gästeliste,
                                 mailController: MailController
                                 ) extends NewsletterSender {
  def sendNewsletter() {
     benutzerRepository.vip map mailController.sendeNewsletter
  }

  def testNewsletter() {
    benutzerRepository.admins map mailController.sendeNewsletter
  }
}
