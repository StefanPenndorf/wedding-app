package net.cyphoria.weddingapp.imagecompare

import org.specs2.mutable.Specification
import org.springframework.core.io.ClassPathResource
import net.cyphoria.weddingapp.imagecompare.specs2.ImageCompareMatchers
import java.awt.image.BufferedImage

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ImageComparatorTest extends Specification with ImageCompareMatchers {

  val bild1: BufferedImage = new ClassPathResource("images/mara_und_lukas.jpg")
  val bild1_mit_kleinem_Fehler: BufferedImage = new ClassPathResource("images/mara_und_lukas_mit_kleinem_Fehler.jpg")
  val bild2: BufferedImage = new ClassPathResource("images/blumen.png")

  "ImageComparator" should  {
      "das identische Bild als identisch erkennen" in {
         bild1 must beSameImageAs (bild1)
      }

      "komplett unterschiedliche Bilder als unterschiedlich erkennen" in {
        bild1 must not (beSameImageAs (bild2))
      }

      "komplett unterschiedliche Bilder als unterschiedlich erkennen (Symmetrie)" in {
        bild2 must not (beSameImageAs (bild1))
      }

      "kaum unterschiedliche Bilder als unterschiedlich erkennen" in {
        bild1 must not (beSameImageAs (bild1_mit_kleinem_Fehler))
      }

      "kaum unterschiedliche Bilder als unterschiedlich erkennen (Symmetrie)" in {
        bild1_mit_kleinem_Fehler must not (beSameImageAs (bild1))
      }

  }

}
