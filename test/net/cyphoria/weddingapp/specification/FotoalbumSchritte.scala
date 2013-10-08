package net.cyphoria.weddingapp.specification

import net.cyphoria.weddingapp.specification.infrastructure.{Browser, Schritte}
import cucumber.api.scala.{DE, ScalaDsl}
import net.cyphoria.weddingapp.specification.seiten.{VipAreaStartSeite, FotoalbenSeite}
import org.springframework.core.io.ClassPathResource
import cucumber.api.PendingException

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbumSchritte extends Schritte with ScalaDsl with DE with Browser {

  val bild = new ClassPathResource("images/mara_und_lukas.jpg")

  def fotoalben = browser.createPage(classOf[FotoalbenSeite])
  def startseite = browser.createPage(classOf[VipAreaStartSeite])

  Angenommen("""^Kerstin ruft die Fotoalben auf$"""){ () =>
    startseite isAt()
    startseite geheZuFotoalbum()
  }

  Wenn("""^sie ein Bild hochlädt$"""){ () =>
    fotoalben upload bild
  }

  Dann("""^wird ein Fotoalbum für sie erstellt$"""){ () =>
    fotoalben oeffneAlbumVon "Kerstin"
  }

  Dann("""^kann sie das Foto anschauen$"""){ () =>
    throw new PendingException()
  }

}
