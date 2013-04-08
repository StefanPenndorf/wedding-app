package views.helper

import views.html.helper.FieldConstructor
import views.html.fieldConstructorTemplate

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
object WeddingHelper {

  implicit val myFields = FieldConstructor(fieldConstructorTemplate.f)

}
