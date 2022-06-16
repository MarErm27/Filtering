package graphql.schemas

import com.google.inject.{Inject, Provider}
import database.dao.UserDAO
import graphql.types.input.FilterInputTypes.FilteringInputType
import graphql.types.output.UserTypes.UsersWithCountType
import graphql.{Arguments, GraphQLContext}
import sangria.schema.{Argument, Field, ListInputType}

class UserSchema @Inject() (
    userDAOProvider: Provider[UserDAO]
) {
  val Queries: List[Field[GraphQLContext, Unit]] = List(
    Field(
      "usersCounts",
      UsersWithCountType,
      arguments = List(
        Arguments.Limit,
        Arguments.Offset,
        Arguments.DateOpt,
        Argument("filters", ListInputType(FilteringInputType))
      ),
      resolve = c =>
        userDAOProvider.get
          .users(c.arg("limit"), c.arg("offset"), c.argOpt("date"))
    )
  )

  val Mutations: List[Field[GraphQLContext, Unit]] = List(
  )
}
