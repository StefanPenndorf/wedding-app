package net.cyphoria.weddingapp.specification.infrastructure

import cucumber.api.scala.ScalaDsl

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait Schritte extends HookRegistering with ScalaDsl {

  HookRegistering.register(this)

  def registerGlobalHooks() {}

}
