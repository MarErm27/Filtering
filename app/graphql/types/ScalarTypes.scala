package graphql.types

import akka.http.scaladsl.model.DateTime
import graphql.violations.{DateTimeCoerceViolation, SafeZoneDateTimeCoerceViolation}
import sangria.ast.StringValue
import sangria.schema.ScalarType
import java.text.SimpleDateFormat
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.util.Date

object ScalarTypes {
  case class SafeZonedDateTime(value: ZonedDateTime)

  implicit val GraphQLDateTime = ScalarType[DateTime](
    "DateTime",
    coerceOutput = (dt, _) => {
      val dateTime = LocalDateTime.parse(dt.toString, DateTimeFormatter.ISO_DATE_TIME)
      val dateTimeZoned = ZonedDateTime.of(dateTime, ZoneId.of("Z").normalized())
      dateTimeZoned.format(DateTimeFormatter.ISO_DATE_TIME)
    },
    coerceInput = {
      case StringValue(dt, _, _, _, _) => DateTime.fromIsoDateTimeString(dt).toRight(DateTimeCoerceViolation)
      case _ => Left(DateTimeCoerceViolation)
    },
    coerceUserInput = {
      case s: String => DateTime.fromIsoDateTimeString(s).toRight(DateTimeCoerceViolation)
      case _ => Left(DateTimeCoerceViolation)
    }
  )

  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.SSSSSS")

  implicit val GraphQLSafeZonedDateTime = ScalarType[SafeZonedDateTime](
    "SafeZonedDateTime",
    coerceOutput = (safeZonedDateTime, _) => {
      val dateTimeZoned = safeZonedDateTime.value.withZoneSameLocal(ZoneId.of("Z").normalized())
      dateTimeZoned.format(DateTimeFormatter.ISO_DATE_TIME)
    },
    coerceInput = {
      case StringValue(safeZonedDateTime, _, _, _, _) => Option(SafeZonedDateTime(ZonedDateTime.parse(safeZonedDateTime, formatter))).toRight(SafeZoneDateTimeCoerceViolation)
      case _ => Left(SafeZoneDateTimeCoerceViolation)
    },
    coerceUserInput = {
      case s: String => Option(SafeZonedDateTime(ZonedDateTime.parse(s, formatter))).toRight(SafeZoneDateTimeCoerceViolation)
      case _ => Left(SafeZoneDateTimeCoerceViolation)
    }
  )

  implicit val GraphQLDate = ScalarType[Date](
    "Date",
    coerceOutput = (dt, _) => new SimpleDateFormat("yyyy-MM-dd").format(dt),
    coerceInput = {
      case StringValue(dt, _, _, _, _) => Right(new SimpleDateFormat("yyyy-MM-dd").parse(dt))
      case _ => Left(DateTimeCoerceViolation)
    },
    coerceUserInput = {
      case s: String => Right(new SimpleDateFormat("yyyy-MM-dd").parse(s))
      case _ => Left(DateTimeCoerceViolation)
    }
  )
}