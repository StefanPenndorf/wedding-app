package modules

import com.tzavellas.sse.guice.ScalaModule
import model.{PersistenteGästeliste, Gästeliste}
import mail.{TemplateMailController, MailController}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ProductionModule extends ScalaModule {
  def configure() {
       bind[MailController].to[TemplateMailController]
       bind[Gästeliste].to[PersistenteGästeliste]
  }
}
