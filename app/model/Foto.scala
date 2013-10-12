package model

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import scala.collection.mutable
import net.sf.jmimemagic.{MagicMatchNotFoundException, Magic}

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
case class Foto(id: Pk[Long] = NotAssigned,
                imageContent: mutable.WrappedArray[Byte] = new Array[Byte](11)) {

  def mimeType = {
    try {
      Magic.getMagicMatch(content, true).getMimeType
    } catch {
      case _:MagicMatchNotFoundException => "application/octet-stream"
    }
  }

  def content = imageContent.array

}


object Foto {

  val simple = {
      get[Long]("fotos.id") ~
      get[Long]("fotos.besitzer") ~
      bytes("fotos.foto") map {
        case id~besitzer~foto => Foto(Id(id), foto)
      }
  }

  implicit def rowToByteArray: Column[Array[Byte]] = Column.nonNull { (value, meta) =>  {
      val MetaDataItem(qualified, _, _) = meta
      value match {
        case data: Array[Byte] => Right(data)
        case cblob: java.sql.Blob => Right(cblob.getBytes(0, cblob.length.toInt))
        case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass + " to Byte Array for column " + qualified))
      }
    }
  }

  def bytes(columnName: String): RowParser[Array[Byte]] = get[Array[Byte]](columnName)(implicitly[Column[Array[Byte]]])

  def findeMitId(id: Long): Option[Foto] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT id,besitzer,foto FROM fotos WHERE id={id}
        """
      ).on(
        'id -> id
      ).as(simple.singleOpt)
    }
  }

}