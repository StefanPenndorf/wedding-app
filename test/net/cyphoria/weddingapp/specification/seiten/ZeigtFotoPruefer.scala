package net.cyphoria.weddingapp.specification.seiten

import net.cyphoria.weddingapp.imagecompare.scalatest.ImageCompareMatchers
import org.fluentlenium.core.FluentPage
import org.springframework.core.io.Resource
import java.awt.image.BufferedImage
import java.util.concurrent.TimeUnit
import java.io.File
import net.cyphoria.weddingapp.imagecompare.FileDownloader
import java.net.URL
import javax.imageio.ImageIO
import org.scalatest.matchers.ShouldMatchers

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait ZeigtFotoPruefer extends ImageCompareMatchers {
  self: FluentPage with ShouldMatchers =>

  final def zeigtFoto(reference: Resource) {
    aktuellesFoto should beSameImageAs(reference)
  }

  def bildLocator: String = "img"

  def aktuellesFoto: BufferedImage = {
    await().atMost(3, TimeUnit.SECONDS).until(bildLocator).areDisplayed()
    val imageSrc = $(bildLocator).first().getAttribute("src")
    imageSrc should include("foto")

    val target = File.createTempFile("image", "")
    // Das ist schlecht und unsicher und sollte durch eine bessere TEMP-Verwaltung ersetzt werden
    target.deleteOnExit()

    new FileDownloader(getDriver).downloadFile(new URL(imageSrc), target)

    ImageIO.read(target)
  }

}
