package database.tables

import models.Filtering

trait ByFiltering {
  def id: slick.lifted.Rep[Int]

  def byFiltering(filtering: Filtering): slick.lifted.Rep[Boolean]
}
