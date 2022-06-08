package models

import sangria.marshalling.{CoercedScalaResultMarshaller, FromInput, ResultMarshaller}
import util.extensions.MapExtensions._

case class Filtering(
                      id: String,
                      like: Option[String],
                      gt: Option[String],
                      lt: Option[String],
                      in: Option[Seq[String]]
                    )

object Filtering {
  implicit val fromInput = new FromInput[Filtering] {
    override val marshaller: ResultMarshaller = CoercedScalaResultMarshaller.default

    override def fromResult(node: marshaller.Node): Filtering = {
      val map = node.asInstanceOf[Map[String, Any]]
      fromMap(map)
    }
  }

  def fromMap(map: Map[String, Any]): Filtering = Filtering(
    map.s("id"),
    map.os("like"),
    map.os("gt"),
    map.os("lt"),
    map.oss("in"),
  )
}