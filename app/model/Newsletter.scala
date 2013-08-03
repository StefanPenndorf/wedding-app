package model

import com.google.inject._
import mail.MailController

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Newsletter {
   def sendNewsletter()
   def testNewsletter()
}


@Singleton
class MailNewsletter  @Inject()(
                                 benutzerRepository: Gästeliste,
                                 mailController: MailController
                                 ) extends Newsletter{
  def sendNewsletter() {
     benutzerRepository.vip map mailController.sendeNewsletter
  }

  def testNewsletter() {
    benutzerRepository.admins map mailController.sendeNewsletter
  }
}
