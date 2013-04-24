package net.cyphoria.weddingapp.specification.infrastructure

import scala.collection.JavaConversions._
import cucumber.api.scala.ScalaDsl
import cucumber.api.Scenario
import com.icegreen.greenmail.util.{ServerSetupTest, GreenMail}
import org.scalatest.matchers.{MatchResult, Matcher}
import javax.mail.Message.RecipientType
import org.scalatest.matchers.ShouldMatchers._
import javax.mail.internet.{MimeMessage, InternetAddress}
import javax.mail.Address
import com.icegreen.greenmail.store.StoredMessage

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait FakeMailer extends RunningApplication with ScalaDsl {

  def mailer = global[GreenMail]("mailServer")

  abstract override def registerGlobalHooks() {
    super.registerGlobalHooks()
    Before { f: (Scenario) => Unit
      val mailer: GreenMail = new GreenMail(ServerSetupTest.SMTP)
      mailer.start()

      registerGlobal("mailServer", mailer)
    }

    After { f: (Scenario) => Unit
      mailer.stop()
      unregisterGlobal("mailServer")
    }
  }

  private def toEMail: (MimeMessage) => EMail = {
    email => EMail(
      email.getFrom()(0).asInstanceOf[InternetAddress].getAddress,
      convertAddresses(email.getRecipients(RecipientType.TO)),
      email.getSubject)
  }

  def receivedEMails() = {
    mailer.getReceivedMessages.toList map toEMail
  }


  def convertAddresses(addrs: Array[Address]): Set[String] = {
    addrs.toSet.map {addr: Address => addr.asInstanceOf[InternetAddress].getAddress}
  }

  def receivedEMailTo(to: String): EMail = {
    val emails = receivedEMailsTo(to: String)
    emails match {
      case x :: List() => x
      case List() => fail("Expected mail to " + to + " be send but no mails were sent!")
      case _ => fail("Expected unique mail to " + to + " but found [" + emails.mkString(",") + "]")
    }
  }

  private def receivedEMailsTo(to: String): List[EMail] = {
    val greenMailUser = mailer.setUser(to, null)
    val inbox = mailer.getManagers.getImapHostManager.getInbox(greenMailUser).getMessages

    List[EMail]() ++ (inbox map { m =>
      m match {
        case s: StoredMessage => toEMail(s.getMimeMessage)
      }
    })
  }

  class HasFromMatcher(val from: String) extends Matcher[EMail] {
    def apply(left: EMail): MatchResult = {
      MatchResult(
        hasFrom(left),
        left + " was not from " + from,
        left + " was from " + from
      )
    }

    private def hasFrom(mail: EMail) : Boolean = {
      from == mail.from
    }
  }

  class HaveSubjectMatcher(val subject: String) extends Matcher[EMail] {
    def apply(left: EMail): MatchResult = {
      MatchResult(
        hasSubject(left),
        left + " has not subject " + subject,
        left + " has subject " + subject
      )
    }

    private def hasSubject(mail: EMail) : Boolean = {
      subject == mail.subject
    }
  }

  def beFrom(from: String): Matcher[EMail] = new HasFromMatcher(from)
  def haveSubject(subject: String): Matcher[EMail] = new HaveSubjectMatcher(subject)
}

case class EMail(from: String, to: Set[String], subject: String)
