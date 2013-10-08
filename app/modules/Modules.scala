package modules

import com.tzavellas.sse.guice.ScalaModule
import model._
import mail.{TemplateMailController, MailController}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ProductionModule extends ScalaModule {
  def configure() {
       bind[MailController].to[TemplateMailController]
       bind[Gästeliste].to[PersistenteGästeliste]
       bind[Hochzeitsplaner].to[HochzeitsplanerImpl]
       bind[NewsletterSender].to[MailNewsletterSender]
       bind[FotoImporter].to[FotoImporterImpl]
       bind[FotoalbenVerwalter].to[PersistenterFotoalbenVerwalter]
  }
}
