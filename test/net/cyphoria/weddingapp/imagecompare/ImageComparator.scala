package net.cyphoria.weddingapp.imagecompare

import java.awt.image.BufferedImage
import org.scalatest.matchers.{MatchResult, Matcher}
import org.springframework.core.io.Resource
import javax.imageio.ImageIO
import org.specs2.matcher.Expectable
import java.util

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class ImageComparator {



}

trait BaseImageCompareMatchers {

  implicit def resourceToBufferedImage(r: Resource): BufferedImage = {
    ImageIO.read(r.getURL)
  }
}

package scalatest {

  trait ImageCompareMatchers extends BaseImageCompareMatchers {

    case class ScalatestImageCompareMatcher(referenceImage: BufferedImage) extends Matcher[BufferedImage] {
      def apply(left: BufferedImage) = {
        MatchResult(
          ImageComparator.imagesAreEqual(referenceImage = referenceImage, actualImage = left),
          "was not same image than reference",
          "was the same image"
        )
      }
    }

    def beSameImageAs(referenceImage: BufferedImage) = new ScalatestImageCompareMatcher(referenceImage)

  }
}

package specs2 {

  trait ImageCompareMatchers extends BaseImageCompareMatchers {

    case class ImageCompareMatcher(referenceImage: BufferedImage) extends org.specs2.matcher.Matcher[BufferedImage] {
      def apply[S <: BufferedImage](s: Expectable[S]) = {
        result(
          ImageComparator.imagesAreEqual(referenceImage = referenceImage, actualImage = s.value),
          "was the same image",
          "was not same image than reference",
          s)
      }
    }

    def beSameImageAs(referenceImage: BufferedImage): org.specs2.matcher.Matcher[BufferedImage] = new ImageCompareMatcher(referenceImage)
  }
}



object ImageComparator {

  def haveSameSize(i1: BufferedImage, i2: BufferedImage): Boolean = {
    i1.getWidth == i2.getWidth && i1.getHeight == i2.getHeight
  }

  def haveSameContent(i1: BufferedImage, i2: BufferedImage): Boolean = {
    val pixel1 = new Array[Int](32*32)
    val pixel2 = new Array[Int](32*32)
    var areEqual = true

    for(y <- Array.range(0, i1.getHeight - 31, 32)) {
      for(x <- Array.range(0, i1.getWidth - 31, 32)) {
        i1.getRGB(x, y, 32, 32, pixel1, 0, 32)
        i2.getRGB(x, y, 32, 32, pixel2, 0, 32)
        areEqual &= util.Arrays.equals(pixel1, pixel2)
      }
    }

    areEqual
  }

  def imagesAreEqual(referenceImage: BufferedImage, actualImage: BufferedImage): Boolean = {
    haveSameSize(referenceImage, actualImage) && haveSameContent(referenceImage, actualImage)
  }

}
