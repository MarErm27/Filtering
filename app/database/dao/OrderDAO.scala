package database.dao

import database.tables.SalesTables
import graphql.adapters.SlickFilter
import models.Filtering
import org.slf4j.LoggerFactory
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import types.{OrderId, UserId}

import java.util.Date
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class OrderDAO @Inject() (
    @NamedDatabase("sales")
    protected val dbConfigProvider: DatabaseConfigProvider,
    filter: SlickFilter
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
    with SalesTables {

  import profile.api._

  val logger = LoggerFactory.getLogger("OrderDAO")

  def orders(ids: Seq[OrderId]) =
    db run OrderT.filter(_.id inSet ids).result

  def ordersByUserID(ids: Seq[UserId]) = {
    db run OrderT.filter(_.userId inSet ids).result
  }

  def orders(limit: Int, offset: Int, date: Option[Date], ids: Seq[Int]) = {
    val baseQuery = OrderT.filter(_.id inSet ids)
    val query = for {
      items <- baseQuery
        .drop(offset)
        .take(limit)
        .result
      count <- baseQuery.length.result
    } yield (items, count)
    db run query
  }

  def ordersWithFilters(
      limit: Int,
      offset: Int,
      date: Option[Date],
      filters: Vector[Filtering]
  ) = {
    db.run(filter.filter(filters, OrderT))
      .flatMap { ids =>
        if (ids.isEmpty) {
          Future((Seq(), 0))
        } else {
          orders(limit, offset, date, ids)
        }
      }
  }
}
