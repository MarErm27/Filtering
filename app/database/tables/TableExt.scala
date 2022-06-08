package database.tables

import akka.http.scaladsl.model.DateTime
import graphql.types.ScalarTypes.{SafeZonedDateTime, formatter}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import util.extensions.DateExtensions
import java.sql.Timestamp
import java.time.LocalDateTime

trait TableExt {
  this: HasDatabaseConfigProvider[JdbcProfile] =>

  import dbConfig.profile.api._

  implicit def dateTimeColumnType = MappedColumnType.base[DateTime, java.sql.Timestamp](
    dt => new Timestamp(dt.clicks),
    ts => DateTime(ts.getTime)
  )

  implicit def ZoneDateTimeToStr(od: SafeZonedDateTime): String = {
    od.value.format(formatter)
  }

  implicit def strToZoneDateTime(s: String): SafeZonedDateTime = {
    SafeZonedDateTime(LocalDateTime.parse(s, formatter).atZone(DateExtensions.getZone))
  }

  implicit val safeZonedDateTimeMapper: BaseColumnType[SafeZonedDateTime] = MappedColumnType.base[SafeZonedDateTime, String](
    ZoneDateTimeToStr,
    strToZoneDateTime
  )
}