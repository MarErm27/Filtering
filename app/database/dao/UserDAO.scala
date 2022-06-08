package database.dao

import database.tables.SalesTables
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import types.UserId
import java.util.Date
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class UserDAO @Inject()(@NamedDatabase("sales")
                        protected val dbConfigProvider: DatabaseConfigProvider
                       )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with SalesTables {

  import profile.api._

  def users(ids: Seq[UserId]) =
    db run UserT.filter(_.id inSet ids).result

  def users(limit: Int, offset: Int, date: Option[Date]) = {
    val query = for {
      items <- UserT
        .drop(offset).take(limit)
        .result
      count <- UserT
        .length
        .result
    } yield (items, count)
    db run query
  }
}