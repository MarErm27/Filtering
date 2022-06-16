package database.dao

import java.util.Date

import scala.concurrent.{ExecutionContext, Future}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import database.tables.SalesTables
import graphql.adapters.SlickFilter
import javax.inject.Inject
import models.Filtering
import types.UserId

class UserDAO @Inject() (
    @NamedDatabase("sales")
    protected val dbConfigProvider: DatabaseConfigProvider,
    filter: SlickFilter
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
    with SalesTables {

  import profile.api._

  def users(ids: Seq[UserId]) =
    db run UserT.filter(_.id inSet ids).result

  def users(
      limit: Int,
      offset: Int,
      date: Option[Date],
      ids: Option[Seq[Int]] = None
  ) = {
    val baseQuery = ids match {
      case Some(value) => UserT.filter(_.id inSet value)
      case None        => UserT
    }
    val query = for {
      items <- baseQuery
        .drop(offset)
        .take(limit)
        .result
      count <- UserT.length.result
    } yield (items, count)
    db run query
  }

  def usersWithFilters(
      filters: Vector[Filtering],
      ids: Seq[Int]
  ) = {
    db.run(filter.filter(filters, OrderT))
      .flatMap { ids =>
        if (ids.isEmpty) {
          Future((Seq(), 0))
        } else {
          users(ids)
        }
      }
  }

}
