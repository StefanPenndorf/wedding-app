package net.cyphoria.weddingapp.specification.infrastructure

import cucumber.api.scala.ScalaDsl
import cucumber.api.Scenario
import com.icegreen.greenmail.util.{ServerSetupTest, GreenMail}
import org.scalatest.matchers.{MatchResult, Matcher}
import javax.mail.Message.RecipientType
import org.scalatest.matchers.ShouldMatchers._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait FakeMailer extends HookRegistering with ScalaDsl {

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

  def receivedEMails() = {
    mailer.getReceivedMessages.toList map {email => EMail(
      email.getFrom()(0).toString,
      email.getRecipients(RecipientType.TO)(0).toString)}
  }

  def receivedEMailTo(to: String): EMail = {
    val emails = receivedEMailsTo(to: String)
    emails match {
      case x :: List() => x
      case List() => fail("Expected mail to " + to + " be send but no mails were sent!")
      case _ => fail("Expected unique mail to " + to + " but found [" + emails.mkString(",") + "]")
    }
  }

  private def receivedEMailsTo(to: String) = {
      receivedEMails() filter (_.to == to)
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
      false
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
      false
    }
  }

  def beFrom(from: String): Matcher[EMail] = new HasFromMatcher(from)
  def haveSubject(subject: String): Matcher[EMail] = new HaveSubjectMatcher(subject)
}

case class EMail(from: String, to: String)
