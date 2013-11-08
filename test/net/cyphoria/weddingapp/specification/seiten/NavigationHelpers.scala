package net.cyphoria.weddingapp.specification.seiten

import org.fluentlenium.core.FluentPage
import java.util.concurrent.TimeUnit

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait NavigationHelpers {
  self: FluentPage =>

  def warteAufUndKlickeDann(element: String) = {
    await().atMost(3, TimeUnit.SECONDS).until(element).isPresent
    $(element).first.click
  }



}
