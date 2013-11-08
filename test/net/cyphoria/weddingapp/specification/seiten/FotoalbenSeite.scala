package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.FluentPage
import org.scalatest.matchers.ShouldMatchers
import org.fluentlenium.core.domain.FluentWebElement
import java.util.concurrent.TimeUnit
import com.google.common.base.Predicate
import org.openqa.selenium.WebDriver
import org.springframework.core.io.Resource
import java.net.URL
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import net.cyphoria.weddingapp.imagecompare.scalatest.ImageCompareMatchers
import java.io.File
import net.cyphoria.weddingapp.imagecompare.FileDownloader
import org.fluentlenium.core.annotation.Page


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbenSeite extends FluentPage with ShouldMatchers with ImageCompareMatchers with NavigationHelpers {

  var bildDatei: FluentWebElement = null
  var starteHochladen: FluentWebElement = null

  @Page
  var fotoalbum: FotoalbumSeiteSeite = null

  def oeffneAlbumVon(besitzer: String) {
    $(s"""#fotoalben a[href*="$besitzer"]""").first.click

    await().atMost(3, TimeUnit.SECONDS).untilPage(fotoalbum).isAt()
    fotoalbum.isAt(besitzer)
    fotoalbum
  }

  def upload(s: Resource) {
    bildDatei.getElement.sendKeys(s.getURI.toString)
    submit(starteHochladen)

    await().atMost(3, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        val messages: String = $(".alert-message.success").getText
        messages != null && messages.contains("Bild(er) erfolgreich zu deinem Album hinzugefügt.")
      }
    })

  }

  def zeigtBild(reference: Resource) {
    aktuellesBild should beSameImageAs(reference)
  }

  private def aktuellesBild: BufferedImage = {
    await().atMost(3, TimeUnit.SECONDS).until("img").areDisplayed()
    val imageSrc = $("img").first().getAttribute("src")
    imageSrc should include("foto")

    val target = File.createTempFile("image", "")
    // Das ist schlecht und unsicher und sollte durch eine bessere TEMP-Verwaltung ersetzt werden
    target.deleteOnExit()

    new FileDownloader(getDriver).downloadFile(new URL(imageSrc), target)

    ImageIO.read(target)
  }

  def naechstesFoto() = warteAufUndKlickeDann("#naechstesBild")

  def vorhergehendesFoto() = warteAufUndKlickeDann("#vorhergehendesBild")

  def waehleFoto(i: Int) = {
    $("img").get(i-1).click()
  }

  def zeigtSeiteMit(anzahlBilder: Int) = {
    await().atMost(10, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        val heading: String = $("h1").getText
        heading != null && heading.contains("Fotoalbum von")
      }
    })

    await().atMost(3, TimeUnit.SECONDS).until("img").hasSize(anzahlBilder)
  }

  def naechsteSeite = warteAufUndKlickeDann("#naechsteSeite")

  override def getUrl: String = "/fotoalben"

  override def isAt() {
    await().atMost(3, TimeUnit.SECONDS).until(new Predicate[WebDriver] {
      def apply(p1: WebDriver): Boolean = {
        title().contains("Steffi und Stefan heiraten!")
      }
    })
    title() should be ("Steffi und Stefan heiraten!")
    $("h1").getText should equal ("Fotoalben")
  }

}
