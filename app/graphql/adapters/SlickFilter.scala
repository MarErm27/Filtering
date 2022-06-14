package graphql.adapters

import database.tables.SalesTables
import models.Filtering
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import slick.lifted.{AbstractTable, TableQuery}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SlickFilter @Inject()(@NamedDatabase("sales")
                            protected val dbConfigProvider: DatabaseConfigProvider
                           )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with SalesTables {

  import profile.api._


  def applyFilters(filter: Filtering) = {

  }

  // https://stackoverflow.com/questions/28281232/slick-create-query-conjunctions-disjunctions-dynamically
  def filter[T <: AbstractTable[_]](filters: Vector[Filtering], tableQuery: TableQuery[T]): Seq[Int] = {
    filters.foldLeft(Seq[Int])((accumulator, filter) =>
      // applyFilters(filter)
      if (false) accumulator :+ 1
      else accumulator
    )
  }
}
