package database.dao

import database.tables.SalesTables
import org.slf4j.LoggerFactory
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import types.{OrderId, UserId}
import java.util.Date
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class OrderDAO @Inject()(@NamedDatabase("sales")
                         protected val dbConfigProvider: DatabaseConfigProvider
                        )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with SalesTables {

  import profile.api._

  val logger = LoggerFactory.getLogger("OrderDAO")

  def orders(ids: Seq[OrderId]) =
    db run OrderT.filter(_.id inSet ids).result

  def orders(limit: Int, offset: Int, date: Option[Date])(filteredIds: Seq[Int]) = {
    val query = for {
      items <- OrderT
        .drop(offset).take(limit)
        .result
      count <- OrderT
        .length
        .result
    } yield (items, count)
    db run query
  }

  def ordersByUserID(ids: Seq[UserId]) = {
    db run OrderT.filter(_.userId inSet ids).result
  }
}