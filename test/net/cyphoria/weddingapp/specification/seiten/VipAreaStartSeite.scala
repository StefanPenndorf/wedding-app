package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.FluentPage
import org.scalatest.matchers.ShouldMatchers
import org.fluentlenium.core.annotation.Page
import java.util.concurrent.TimeUnit

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class VipAreaStartSeite extends FluentPage with ShouldMatchers {

  @Page
  var fotoalben: FotoalbenSeite = null


  def geheZuFotoalbum() {
    $("""#navigation a[id="nav-fotoalben"]""").first.click
    await().atMost(3, TimeUnit.SECONDS).untilPage(fotoalben).isAt()
    fotoalben
  }

  override def getUrl: String = "/vip"

  override def isAt() {
    title() should be("Steffi und Stefan heiraten!")
    $("h1").getText should startWith("Willkommen")
    $("body").getText should include("Hochzeit")
    $("body").getText should include("Steffi & Stefan")

  }

}
