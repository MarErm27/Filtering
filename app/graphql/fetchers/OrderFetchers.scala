package graphql.fetchers

import graphql.GraphQLContext
import models.Order
import sangria.execution.deferred.{Fetcher, HasId, Relation, RelationIds}
import types.{OrderId, UserId}

object OrderFetchers {
  val OrdersFetcher = Fetcher((ctx: GraphQLContext, ids: Seq[OrderId]) => ctx.dao.orderDAO.orders(ids))

  val OrderByUserRelation = Relation[Order, UserId]("byUserId", orders => Seq(orders.userId))
  val OrderFetcher = Fetcher.rel(
    (ctx: GraphQLContext, ids: Seq[OrderId]) => ctx.dao.orderDAO.orders(ids),
    (ctx: GraphQLContext, ids: RelationIds[Order]) => ctx.dao.userDAO.users(ids(OrderByUserRelation))
  )(HasId(_.userId))

  val Fetchers = Seq(
    OrdersFetcher,
    OrderFetcher
  )
}