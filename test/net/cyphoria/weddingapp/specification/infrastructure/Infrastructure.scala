package net.cyphoria.weddingapp.specification.infrastructure

import cucumber.api.scala.ScalaDsl
import cucumber.api.Scenario
import com.icegreen.greenmail.util.{ServerSetupTest, GreenMail}
import org.slf4j.LoggerFactory

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class Infrastructure extends ScalaDsl with HookRegistering {

  val logger = LoggerFactory.getLogger("test-infras")

  def mailer = global[GreenMail]("mailServer")

  Before { f: (Scenario) => Unit
    logger.error("Starting mailer2..." + Thread.currentThread().toString)
    val mailer: GreenMail = new GreenMail(ServerSetupTest.SMTP)
    mailer.start()
    logger.error("... started" + Thread.currentThread().toString)
    registerGlobal("mailServer", mailer)
  }

  After { f: (Scenario) => Unit
    logger.error("Stopping mailer 2"  + Thread.currentThread().toString)

    global[GreenMail]("mailServer").stop()
    unregisterGlobal("mailServer")
  }

  def registerGlobalHooks() {}
}
