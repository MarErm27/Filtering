package graphql.types.output

import graphql.GraphQLContext
import graphql.fetchers.UserFetchers
import models.Order
import sangria.macros.derive.{AddFields, deriveObjectType}
import sangria.schema.{Field, IntType, ListType, ObjectType, fields}
import graphql.types.output.UserTypes.UserType
import graphql.types.ScalarTypes._

object OrderTypes {
  implicit val ec = scala.concurrent.ExecutionContext.global

  implicit lazy val OrderType = deriveObjectType[GraphQLContext, Order](
    AddFields(
      Field("userInfo", UserType,
        resolve = c => UserFetchers.UsersFetcher.defer(c.value.id))
    )
  )

  lazy val OrdersWithCountType = ObjectType("OrdersWithCount",
    fields[GraphQLContext, (Seq[Order], Int)](
      Field("ordersList", ListType(OrderType), resolve = _.value._1),
      Field("count", IntType, resolve = _.value._2),
    )
  )
}