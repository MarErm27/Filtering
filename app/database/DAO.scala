package database

import database.dao.{OrderDAO, UserDAO}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class DAO @Inject()(@NamedDatabase("sales") protected val dbConfigProvider: DatabaseConfigProvider)
                   (
                     val userDAO: UserDAO,
                     val orderDAO: OrderDAO
                   )
                   (implicit val ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

}