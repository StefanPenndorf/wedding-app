package net.cyphoria.weddingapp

import play.api.test.Helpers._
import play.api.test.FakeApplication

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
package object functional {

  def laufenderAnwendung[T](block: => T) = running(FakeApplication(additionalConfiguration = inMemoryDatabase()))(block)

}
