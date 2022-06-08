package graphql

import graphql.types.ScalarTypes._
import sangria.schema.{Argument, IntType, OptionInputType}

object Arguments {
  val Offset = Argument("offset", IntType, defaultValue = 0)
  val Limit = Argument("limit", IntType, defaultValue = 5)
  val DateOpt = Argument("date", OptionInputType(GraphQLDate))
}