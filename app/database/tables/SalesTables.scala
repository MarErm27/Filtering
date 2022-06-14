package database.tables

import akka.http.scaladsl.model.DateTime
import graphql.types.ScalarTypes.SafeZonedDateTime
import models.{Order, User}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.Filtering
import java.time.ZonedDateTime


trait ByFiltering {
  def id: slick.lifted.Rep[Int]
  def byFiltering(filtering: Filtering): slick.lifted.Rep[Boolean]
}

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

    def * = (
      id,
      email,
      password,
      verified,
      birthdate,
      createdAt,
      updatedAt,
      isDeleted
    ).mapTo[User]
  }

  class OrderTable(tag: Tag) extends Table[Order](tag, "order") with ByFiltering {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def userId = column[Int]("user_id")

    def expectedDeliveryDate = column[DateTime]("expected_delivery_date")

    def createdAt = column[Option[SafeZonedDateTime]]("created_at")

    def updatedAt = column[Option[SafeZonedDateTime]]("updated_at")

    def isDeleted = column[Boolean]("is_deleted")

    def byFiltering(filtering: Filtering) = filtering match {
      case Filtering("name", like, gt, lt, in) =>
        like.fold[Rep[Boolean]](true)(name.like(_)) &&
        gt.fold[Rep[Boolean]](true)(name > _) &&
        lt.fold[Rep[Boolean]](true)(name < _) &&
        in.fold[Rep[Boolean]](true)(name.inSet(_))
      case Filtering("createdAt", like, gt, lt, in) =>
        // like.fold[Rep[Boolean]](true)(createdAt.like(_)) &&
        gt.fold[Rep[Boolean]](true) { string =>
          createdAt.map(_ > SafeZonedDateTime(ZonedDateTime.parse(string))).getOrElse(false)
        } &&
        gt.fold[Rep[Boolean]](true) { string =>
          createdAt.map(_ < SafeZonedDateTime(ZonedDateTime.parse(string))).getOrElse(false)
        } /* &&
        in.fold[Rep[Boolean]](true)(createdAt.inSet(_)) */
    }

    def * =
      (id, name, userId, expectedDeliveryDate, createdAt, updatedAt, isDeleted)
        .mapTo[Order]
  }
}
