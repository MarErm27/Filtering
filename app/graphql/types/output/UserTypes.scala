package graphql.types.output

import graphql.GraphQLContext
import graphql.fetchers.OrderFetchers
import models.User
import sangria.macros.derive.{AddFields, deriveObjectType}
import sangria.schema.{Field, IntType, ListType, ObjectType, OptionType, fields}
import graphql.types.output.OrderTypes.OrderType
import graphql.types.ScalarTypes._

object UserTypes {
  implicit val ec = scala.concurrent.ExecutionContext.global
  implicit lazy val UserType = deriveObjectType[GraphQLContext, User](
    //    AddFields(
    //      Field("orders", ListType(OrderType),
    //        resolve = c => OrderFetchers.OrderFetcher.deferRelSeq(OrderFetchers.OrderByUserRelation, c.value.id))
    //    )
  )

  lazy val UsersWithCountType = ObjectType("UsersWithCount",
    fields[GraphQLContext, (Seq[User], Int)](
      Field("list", ListType(UserType), resolve = _.value._1),
      Field("count", IntType, resolve = _.value._2),
    )
  )
}