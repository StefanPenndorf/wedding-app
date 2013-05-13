package net.cyphoria.weddingapp.specification.pages

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.domain.FluentWebElement
import org.scalatest.matchers.ShouldMatchers
import net.cyphoria.weddingapp.specification.users.User
import org.fluentlenium.core.annotation.Page

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class IndexPage extends FluentPage with ShouldMatchers {

  var loginname: FluentWebElement = null

  @Page
  var vipAreaWelcomePage: VipAreaWelcomePage = null

  override def getUrl: String = "/"

  override def isAt {
    title() should be ("Steffi und Stefan heiraten!")
    $("h1").getText should equal ("Steffi und Stefan heiraten!")
  }

  def loginAs(user: User) = {
    fill(loginname) `with` user.email
    fill("#passwort") `with` user.passwort
    submit("#login")
    vipAreaWelcomePage.isAt()
    vipAreaWelcomePage
  }


}
