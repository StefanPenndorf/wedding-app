package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.FluentPage
import org.scalatest.matchers.ShouldMatchers
import org.fluentlenium.core.domain.FluentWebElement
import java.util.concurrent.TimeUnit
import com.google.common.base.Predicate
import org.openqa.selenium.WebDriver
import org.springframework.core.io.Resource

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbenSeite extends FluentPage with ShouldMatchers {
  var bildDatei: FluentWebElement = null
  var starteHochladen: FluentWebElement = null

  def oeffneAlbumVon(s: String) {
    $(s"""#fotoalben a[href*="$s"]""").first.click

    await().atMost(50, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        val heading: String = $("h1").getText
        heading != null && heading.contains(s"""Fotoalbum von $s""")
      }
    })

    $("img").first() should be ('displayed)
  }

  def upload(s: Resource) {
    bildDatei.getElement.sendKeys(s.getURI.toString)
    submit(starteHochladen)

    await().atMost(3, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        val messages: String = $(".alert-message.success").getText
        messages != null && messages.contains("Bild erfolgreich zu deinem Album hinzugefügt.")
      }
    })

  }

  override def getUrl: String = "/fotoalben"

  override def isAt() {
    title() should be ("Steffi und Stefan heiraten!")
    $("h1").getText should equal ("Fotoalben")
  }

}
