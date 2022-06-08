package graphql.violations

import sangria.validation.Violation

case class TooComplexQueryError(msg: String = "Query is too expensive.") extends Exception(msg)

case class UnsupportedBodyTypeError(msg: String) extends Exception(msg)

case object DateTimeCoerceViolation extends Violation {
  override def errorMessage: String = "Error during parsing DateTime"
}

case object SafeZoneDateTimeCoerceViolation extends Violation {
  override def errorMessage: String = "Error during parsing SafeZoneDateTime"
}