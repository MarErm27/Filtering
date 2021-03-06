package graphql.schemas

import com.google.inject.{Inject, Provider}
import database.dao.OrderDAO
import graphql.types.input.FilterInputTypes._
import graphql.types.output.OrderTypes.OrdersWithCountType
import graphql.{Arguments, GraphQLContext}
import sangria.schema.{Argument, Field, ListInputType}

class OrderSchema @Inject()(
                             ordersDAOProvider: Provider[OrderDAO]
                           ) {

  val Queries: List[Field[GraphQLContext, Unit]] = List(
    Field("ordersCounts", OrdersWithCountType,
      arguments = List(Arguments.Limit, Arguments.Offset, Arguments.DateOpt, Argument("filters", ListInputType(FilteringInputType))),
      resolve = c => ordersDAOProvider.get.ordersWithFilters(c.arg("limit"), c.arg("offset"), c.argOpt("date"), c.arg("filters")),
    ),
  )

  val Mutations: List[Field[GraphQLContext, Unit]] = List(
  )
}
