package graphql

import com.google.inject.Inject
import graphql.schemas.{OrderSchema, UserSchema}
import sangria.schema.{ObjectType, fields}

class GraphQL @Inject()(
                         userSchema: UserSchema,
                         orderSchema: OrderSchema
                       ) {
  val Schema = sangria.schema.Schema(
    query = ObjectType("Query",
      fields[GraphQLContext, Unit](
        userSchema.Queries
          ++ orderSchema.Queries
          : _*
      )
    ),
    //    mutation = Some(
    //      ObjectType("Mutation",
    //        fields[GraphQLContext, Unit](
    //          userSchema.Mutations
    //            ++ orderSchema.Mutations
    //            : _*
    //        )
    //      )
    //    )
  )
}