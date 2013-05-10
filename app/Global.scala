import com.google.inject.Guice
import modules.ProductionModule
import play.api._


/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object Global extends GlobalSettings {

  private lazy val injector = Guice.createInjector(new ProductionModule)

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }
}
