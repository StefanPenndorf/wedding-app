package net.cyphoria.weddingapp.specification

import cucumber.api.scala.{DE, ScalaDsl}
import org.scalatest.matchers.ShouldMatchers._


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ImpressumSchritte extends ScalaDsl with DE with Browser {

  Angenommen("""^ein Benutzer möchte das Impressum sehen$"""){ () =>
  }

  Wenn("""^er das Impressum aufruft$"""){ () =>
      browser.goTo("/impressum")
  }

  Dann("""^wird das Impressum angezeigt$"""){ () =>
    browser.$("h1").getText should equal ("Impressum")

    browser.$("#impressum").getText should include ("Stefan Penndorf")
  }
}
