import model.BenutzerName
import play.api.mvc.PathBindable

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
package object binders {
  implicit def benutzerNamePathBinder(implicit stringBinder: PathBindable[String]) = new PathBindable[BenutzerName] {
    override def bind(key: String, value: String): Either[String, BenutzerName] = {
      value.split("\\.") match {
        case Array(vorname, nachname) => Right(BenutzerName(vorname, nachname))
        case _ => Left("Ungültiger Benutzer")
      }
    }
    override def unbind(key: String, benutzerName: BenutzerName): String = {
      stringBinder.unbind(key, benutzerName.vorname + '.' + benutzerName.nachname)
    }
  }

}
