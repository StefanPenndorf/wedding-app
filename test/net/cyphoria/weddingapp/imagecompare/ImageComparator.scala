package net.cyphoria.weddingapp.imagecompare

import java.awt.image.BufferedImage
import org.scalatest.matchers.{MatchResult, Matcher}
import org.springframework.core.io.Resource
import javax.imageio.ImageIO
import org.specs2.matcher.Expectable

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

  def imagesAreEqual(referenceImage: BufferedImage, actualImage: BufferedImage): Boolean = {
    false
  }

}
