package graphql.fetchers

import graphql.GraphQLContext
import sangria.execution.deferred.Fetcher
import types.UserId

object UserFetchers {
  val UsersFetcher = Fetcher((ctx: GraphQLContext, ids: Seq[UserId]) => ctx.dao.userDAO.users(ids))

  val Fetchers = Seq(
    UsersFetcher
  )
}