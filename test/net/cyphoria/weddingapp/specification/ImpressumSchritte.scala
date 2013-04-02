package net.cyphoria.weddingapp.specification

import cucumber.api.scala.{DE, ScalaDsl}
import org.specs2.matcher.ShouldMatchers

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ImpressumSchritte extends ScalaDsl with DE with Browser with ShouldMatchers {

  Angenommen("""^ein Benutzer möchte das Impressum sehen$"""){ () =>
  }

  Wenn("""^er das Impressum aufruft$"""){ () =>
      browser.goTo("/")
      Thread.sleep(4000)
  }

  Dann("""^wird das Impressum angezeigt$"""){ () =>
    throw new RuntimeException()
  }
}
