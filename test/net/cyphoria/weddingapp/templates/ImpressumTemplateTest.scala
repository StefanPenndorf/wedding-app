package net.cyphoria.weddingapp.templates

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.FakeApplication

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ImpressumTemplateTest extends Specification {

  val impressum = running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      views.html.impressum()
  }

  "Das Impressum" should {

      "Suchmaschinen das Indizieren nicht erlaubten" in {
         impressum.body must contain("<meta name=\"robots\" content=\"noindex")
       }

       "den Namen und die Anschrift des Dienstanbieters enthalten" in  {
         impressum.body must contain("Stefan Penndorf")
         impressum.body must contain("Luckaer Str. 22")
         impressum.body must contain("04229 Leipzig")
       }

       "eine Kontaktmöglichkeit für die unmittelbare Kontaktaufnahme enthalten" in {
         impressum.body must contain("stefan at cyphoria dot net")
         impressum.body must contain("0151/11576308")
       }

  }

}
