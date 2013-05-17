package net.cyphoria.weddingapp.functional

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeRequest, FakeApplication}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ApplicationControllerTest extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "render the index page" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val home = route(FakeRequest(GET, "/")).get

        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain ("Steffi und Stefan heiraten!")
      }
    }

  }

}
