package net.cyphoria.weddingapp.specification

import net.cyphoria.weddingapp.specification.infrastructure.{Browser, Schritte}
import cucumber.api.scala.{DE, ScalaDsl}
import net.cyphoria.weddingapp.specification.seiten.{VipAreaStartSeite, FotoalbenSeite}
import org.springframework.core.io.ClassPathResource

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FotoalbumSchritte extends Schritte with ScalaDsl with DE with Browser {

  val bild = new ClassPathResource("images/mara_und_lukas.jpg")
  val bild1 = bild
  val bild2 = new ClassPathResource("images/blumen.png")
  val bild3 = new ClassPathResource("images/mara_und_lukas_mit_kleinem_Fehler.jpg")

  def fotoalben = browser.createPage(classOf[FotoalbenSeite])
  def startseite = browser.createPage(classOf[VipAreaStartSeite])

  Angenommen("""^Kerstin ruft die Fotoalben auf$"""){ () =>
    startseite isAt()
    startseite geheZuFotoalbum()
  }

  Angenommen("""^Kerstin hat drei Bilder hochgeladen$"""){ () =>
    startseite isAt()
    startseite geheZuFotoalbum()

    Seq(bild1, bild2, bild3).foreach({cp => fotoalben upload cp; fotoalben go()})
  }

  Angenommen("""^Kerstin hat 21 Bilder hochgeladen$"""){ () =>
    startseite isAt()
    startseite geheZuFotoalbum()

    (Seq.fill(20)(bild2) :+ bild1).foreach({cp => fotoalben upload cp; fotoalben go()})
  }

  Angenommen("""^Kerstin ruft das erste Foto auf$"""){ () =>
    fotoalben oeffneAlbumVon "Kerstin"
    fotoalben waehleFoto 1
  }

  Wenn("""^sie ein Bild hochlädt$"""){ () =>
    fotoalben upload bild
  }

  Wenn("""^sie ein Bild weiter blättert$"""){ () =>
    fotoalben naechstesFoto()
  }

  Wenn("""^sie ein Bild zurück blättert$"""){ () =>
    fotoalben vorhergehendesFoto()
  }

  Wenn("""sie ihr Fotoalbum aufruft"""){ () =>
    fotoalben oeffneAlbumVon "Kerstin"
  }

  Wenn("""^auf die zweite Seite blättert$"""){ () =>
    fotoalben naechsteSeite
  }

  Dann("""^wird ein Fotoalbum für sie erstellt$"""){ () =>
    fotoalben oeffneAlbumVon "Kerstin"
  }

  Dann("""^kann sie das erste Foto anschauen$"""){ () =>
    fotoalben zeigtBild bild
  }

  Dann("""^kann sie das zweite Foto anschauen$"""){ () =>
    fotoalben zeigtBild bild2
  }

  Dann("""^kann sie die erste Seite ihres Fotoalbums mit den drei Bildern sehen$"""){ () =>
    fotoalben zeigtSeiteMit(anzahlBilder = 3)
  }

  Dann("""^kann sie die zweite Seite ihres Fotoalbums sehen$"""){ () =>
    fotoalben zeigtSeiteMit(anzahlBilder = 1)
    fotoalben zeigtBild bild1
  }

}
