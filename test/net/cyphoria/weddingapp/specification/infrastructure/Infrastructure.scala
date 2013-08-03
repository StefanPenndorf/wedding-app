package net.cyphoria.weddingapp.specification.infrastructure

import cucumber.api.scala.ScalaDsl
import cucumber.api.Scenario
import com.icegreen.greenmail.util.{ServerSetupTest, GreenMail}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class Infrastructure extends ScalaDsl with HookRegistering {

  def mailer = global[GreenMail]("mailServer")

  Before { f: (Scenario) => Unit
    val mailer: GreenMail = new GreenMail(ServerSetupTest.SMTP)
    mailer.start()
    registerGlobal("mailServer", mailer)
  }

  After { f: (Scenario) => Unit
    global[GreenMail]("mailServer").stop()
    unregisterGlobal("mailServer")
  }

  def registerGlobalHooks() {}
}
