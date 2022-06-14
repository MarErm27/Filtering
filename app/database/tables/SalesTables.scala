package database.tables

import akka.http.scaladsl.model.DateTime
import graphql.types.ScalarTypes.SafeZonedDateTime
import models.{Order, User}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait SalesTables extends TableExt {
  this: HasDatabaseConfigProvider[JdbcProfile] =>

  import dbConfig.profile.api._

  val UserT = TableQuery[UserTable]
  val OrderT = TableQuery[OrderTable]

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def email = column[String]("email")

    def password = column[Option[String]]("password", O.Default(None))

    def verified = column[Boolean]("verified")

    def birthdate = column[DateTime]("birthdate")

    def createdAt = column[Option[SafeZonedDateTime]]("created_at")

    def updatedAt = column[Option[SafeZonedDateTime]]("updated_at")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id, email, password, verified, birthdate, createdAt, updatedAt, isDeleted).mapTo[User]
  }

  class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def userId = column[Int]("user_id")

    def expectedDeliveryDate = column[DateTime]("expected_delivery_date")

    def createdAt = column[Option[SafeZonedDateTime]]("created_at")

    def updatedAt = column[Option[SafeZonedDateTime]]("updated_at")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id, name, userId, expectedDeliveryDate, createdAt, updatedAt, isDeleted).mapTo[Order]
  }
}