package util.extensions

import akka.http.scaladsl.model.DateTime
import java.time.LocalDate
import java.util.UUID
import scala.collection.immutable.ListMap

object MapExtensions {
  implicit class MapExtended(map: Map[String, Any]) {
    def getOrElseAsOpt[T](key: String, default: T): Option[T] =
      map.getOrElse(key, Some(default)).asInstanceOf[Option[T]]
    def getOrElseAs[T](key: String, default: T): T =
      getOrElseAsOpt(key, default).get
    def getStringId(key: String): String =
      getOrElseAs(key, UUID.randomUUID().toString)

    def o[T](key: String): Option[T] =
      map.get(key).flatMap(_.asInstanceOf[Option[T]])
    def os(key: String): Option[String] =
      map.get(key).flatMap(_.asInstanceOf[Option[String]])
    def oi(key: String): Option[Int] =
      map.get(key).flatMap(_.asInstanceOf[Option[Int]])
    def ob(key: String): Option[Boolean] =
      map.get(key).flatMap(_.asInstanceOf[Option[Boolean]])
    def odbl(key: String): Option[Double] =
      map.get(key).flatMap(_.asInstanceOf[Option[Double]])
    def od(key: String): Option[LocalDate] =
      map.get(key).flatMap(_.asInstanceOf[Option[LocalDate]])
    def ol(key: String): Option[ListMap[String, Any]] =
      map.get(key).flatMap(_.asInstanceOf[Option[ListMap[String, Any]]])
    def osl(key: String): Seq[ListMap[String, Any]] =
      map.get(key).flatMap(_.asInstanceOf[Option[Seq[ListMap[String, Any]]]]).getOrElse(Seq.empty)
    def odt(key: String): Option[DateTime] =
      map.get(key).flatMap(_.asInstanceOf[Option[DateTime]])

    def s(key: String): String =
      map(key).asInstanceOf[String]
    def i(key: String): Int =
      map(key).asInstanceOf[Int]
    def b(key: String): Boolean =
      map(key).asInstanceOf[Boolean]
    def d(key: String): LocalDate =
      map(key).asInstanceOf[LocalDate]
    def l(key: String): ListMap[String, Any] =
      map(key).asInstanceOf[ListMap[String, Any]]
    def ss(key: String): Seq[String] =
      map(key).asInstanceOf[Seq[String]]
    def ovs(key: String): Option[Vector[String]] = {
      val value = map.get(key)

      map.get(key).asInstanceOf[Option[Vector[String]]]
    }
    def si(key: String): Seq[Int] =
      map(key).asInstanceOf[Seq[Int]]
    def sl(key: String): Seq[ListMap[String, Any]] =
      map(key).asInstanceOf[Seq[ListMap[String, Any]]]
  }
}