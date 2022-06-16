package graphql.adapters

import database.tables.{ByFiltering, SalesTables}
import models.Filtering
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
//import slick.lifted.AbstractTable

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SlickFilter @Inject() (
    @NamedDatabase("sales")
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
    with SalesTables {

  import profile.api._

  // https://stackoverflow.com/questions/28281232/slick-create-query-conjunctions-disjunctions-dynamically
  def filter[Row <: ByFiltering](
      filters: Vector[Filtering],
      query: Query[Row, _, Seq]
  ): DBIO[Seq[Int]] = {
    val result =
      query
        .filter { table =>
          filters.foldLeft[Rep[Boolean]](true)((accumulator, filter) =>
            accumulator && table.byFiltering(filter)
          )
        }
        .map(_.id)
        .result
    println(result.statements.mkString)
    result
  }
}
