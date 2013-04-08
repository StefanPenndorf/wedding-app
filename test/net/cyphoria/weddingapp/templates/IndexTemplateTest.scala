package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class IndexTemplateTest extends Specification {

  val index = views.html.index()

  "Die Startseite" should {

    "einen Link zum Impressum enthalten" in {
      index.body must contain("""href="/impressum"""")
      index.body must contain("Impressum")
    }

    "einen Link zum Registrieren enthalten" in {
      index.body must contain("""href="/register"""")
      index.body.toLowerCase must contain("registrieren")
    }

  }

}
