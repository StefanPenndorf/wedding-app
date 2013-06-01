package controllers

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
sealed trait Berechtigung
case object GastBerechtigung extends Berechtigung
case object AdminBerechtigung extends Berechtigung
