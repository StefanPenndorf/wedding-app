package controllers

import jp.t2v.lab.play2.auth.AuthConfig
import model.{PersistenteGästeliste, Benutzer}
import scala.reflect.ClassTag
import reflect.classTag
import play.api.mvc.{Result, RequestHeader}
import play.api.mvc.Results._

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
trait WeddingAuthConfig extends AuthConfig {

  /**
   * A type that is used to identify a user.
   * `String`, `Int`, `Long` and so on.
   */
  type Id = Long

  /**
   * A type that represents a user in your application.
   * `User`, `Account` and so on.
   */
  type User = Benutzer

  /**
   * A type that is defined by every action for authorization.
   * This sample uses the following trait:
   *
   * sealed trait Permission
   * case object Administrator extends Permission
   * case object NormalUser extends Permission
   */
  type Authority = Permission

  /**
   * A `ClassManifest` is used to retrieve an id from the Cache API.
   * Use something like this:
   */
  val idTag: ClassTag[Id] = classTag[Id]

  /**
   * The session timeout in seconds
   */
  val sessionTimeoutInSeconds = 0

  /**
   * A function that returns a `User` object from an `Id`.
   * You can alter the procedure to suit your application.
   */
  def resolveUser(id: Id): Option[User] = new PersistenteGästeliste().findeGastMitId(id)

  /**
   * Where to redirect the user after a successful login.
   */
  def loginSucceeded(request: RequestHeader): Result = Redirect(routes.VipArea.viparea())

  /**
   * Where to redirect the user after logging out
   */
  def logoutSucceeded(request: RequestHeader): Result = Redirect(routes.Application.index())

  /**
   * If the user is not logged in and tries to access a protected resource then redirct them as follows:
   */
  def authenticationFailed(request: RequestHeader): Result = Redirect(routes.Application.index())

  /**
   * If authorization failed (usually incorrect password) redirect the user as follows:
   */
  def authorizationFailed(request: RequestHeader): Result = Forbidden("no permission")


  /**
   * A function that determines what `Authority` a user has.
   * You should alter this procedure to suit your application.
   */
  def authorize(user: User, authority: Authority): Boolean = false

  /**
   * Whether use the secure option or not use it in the cookie.
   * However default is false, I strongly recommend using true in a production.
   */
  override lazy val cookieSecureOption: Boolean =
    play.api.Play.current.configuration.getBoolean("auth.cookie.secure").getOrElse(true)

}
