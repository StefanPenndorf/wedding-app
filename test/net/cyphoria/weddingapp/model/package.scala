package net.cyphoria.weddingapp

import play.api.test.Helpers._
import play.api.test.FakeApplication
import javax.sql.DataSource
import play.api.db.DB
import scaladbtest.test.ScalaDbTester

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
package object model {

  def mitDatenbank[T](block: => T) = running(FakeApplication(additionalConfiguration = inMemoryDatabase()))(block)

  def DatenbankMit[T](fixtureFileName: String)(block: => T) {
    val application = FakeApplication(additionalConfiguration = inMemoryDatabase())
    val source: DataSource = DB.getDataSource()(application)
    val scaladbtester = new ScalaDbTester(source, "test/resources/model")
    running(application) {
      scaladbtester.onBefore(fixtureFileName + ".dbt")
      block
      scaladbtester.onAfter()
    }
  }

}
