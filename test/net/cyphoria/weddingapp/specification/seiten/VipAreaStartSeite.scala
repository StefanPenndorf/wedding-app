package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.FluentPage
import org.scalatest.matchers.ShouldMatchers

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class VipAreaStartSeite extends FluentPage with ShouldMatchers {

  override def getUrl: String = "/vip"

  override def isAt() {
    title() should be("Steffi und Stefan heiraten!")
    $("h1").getText should startWith("Willkommen")
    $("body").getText should include("Trauzeugin")
    $("body").getText should include("Steffi & Stefan")

  }

}
