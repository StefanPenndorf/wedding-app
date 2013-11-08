package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.FluentPage
import org.scalatest.matchers.ShouldMatchers
import java.util.concurrent.TimeUnit

/**
 * Seitenrepräsentation einer Seite eines Fotoalbums eines Benutzers.
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbumSeiteSeite extends FluentPage with ShouldMatchers with ZeigtFotoPruefer with NavigationHelpers  {


  override def getUrl: String = "/fotoalbum/<Besitzer>"

  override def isAt() = isAt("")

  def isAt(besitzer: String) {
    await().atMost(5, TimeUnit.SECONDS).until("h1").withText().contains(s"""Fotoalbum von $besitzer""").isPresent

    $("img").first() should be ('displayed)
  }




}
