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
  val bild2: BufferedImage = new ClassPathResource("images/blumen.png")

  "ImageComparator" should  {
      "das identische Bild als identisch erkennen" in {
         bild1 must beSameImageAs (bild1)
      }


  }

}
