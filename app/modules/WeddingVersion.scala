package modules

import play.api.Play

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object WeddingVersion {

  def version =
    Play.maybeApplication match {
      case None => "test"
      case Some(application) => application.configuration.getString("app.version")
    }

}
