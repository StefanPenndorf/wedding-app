package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.annotation.Page
import org.fluentlenium.core.FluentPage

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class Navigation extends FluentPage {

  @Page
  var fotoalbum: FotoalbenSeite = null

  def geheZuFotoalbum(): FotoalbenSeite = {
    $("""#navigation a[id="nav-fotoalben"]""").first.click
    fotoalbum isAt()
    fotoalbum
  }


}
