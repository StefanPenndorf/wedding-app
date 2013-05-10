package modules

import com.tzavellas.sse.guice.ScalaModule
import model.{BenutzerDatabaseRepository, BenutzerRepository}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ProductionModule extends ScalaModule {
  def configure() {
       bind[BenutzerRepository].to[BenutzerDatabaseRepository]
  }
}
