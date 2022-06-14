package models

import graphql.types.ScalarTypes.SafeZonedDateTime
import models.traits.Identifiable
import sangria.macros.derive.GraphQLExclude
import akka.http.scaladsl.model.DateTime

case class User(
                 id: Int = 0,
                 email: String,
                 @GraphQLExclude
                 password: Option[String] = None,
                 verified: Boolean,
                 birthdate: DateTime,
                 createdAt: Option[SafeZonedDateTime],
                 updatedAt: Option[SafeZonedDateTime],
                 isDeleted: Boolean
               ) extends Identifiable

object User {
  val tupled = (this.apply _).tupled
}