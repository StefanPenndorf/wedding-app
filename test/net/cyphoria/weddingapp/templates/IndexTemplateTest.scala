package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeApplication
import controllers.Application

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class IndexTemplateTest extends Specification {

  val index = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    views.html.index(Application.loginFormular)
  }

  "Die Startseite" should {

    "einen Link zum Impressum enthalten" in {
      index.body must contain("""href="/impressum"""")
      index.body must contain("Impressum")
    }

    "einen Link zum Registrieren enthalten" in {
      index.body must contain("""href="/register"""")
      index.body.toLowerCase must contain("registrieren")
    }

    "das Loginformular enthalten" in {
      index.body must contain("""id="loginformular""")
      index.body must contain("Passwort")
      index.body must contain("Login")
    }

  }

}
