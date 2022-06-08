package graphql.schemas

import com.google.inject.{Inject, Provider}
import database.dao.OrderDAO
import graphql.{Arguments, GraphQLContext}
import sangria.schema.{Argument, Field, ListInputType}
import graphql.types.input.FilterInputTypes._
import graphql.types.output.OrderTypes.OrdersWithCountType

class OrderSchema @Inject()(
                             ordersDAOProvider: Provider[OrderDAO]
                           ) {
  val Queries: List[Field[GraphQLContext, Unit]] = List(
    Field("ordersCounts", OrdersWithCountType,
      arguments = List(Arguments.Limit, Arguments.Offset, Arguments.DateOpt, Argument("filters", ListInputType(FilteringInputType))),
      resolve = c => ordersDAOProvider.get.orders(c.arg("limit"), c.arg("offset"), c.argOpt("date")),
    ),
  )

  val Mutations: List[Field[GraphQLContext, Unit]] = List(
  )
}