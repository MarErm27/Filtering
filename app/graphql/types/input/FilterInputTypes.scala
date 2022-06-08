package graphql.types.input

import graphql.types.ScalarTypes._
import models.Filtering
import sangria.macros.derive.deriveInputObjectType

object FilterInputTypes {
  implicit lazy val FilteringInputType = deriveInputObjectType[Filtering]()
}